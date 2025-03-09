package com.nocountry.urbia.service.integration;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.Base64;

import java.net.URI;
import org.springframework.web.util.UriComponentsBuilder;


@Service
public class GeminiService {

    // La API key se configurará en application.properties
    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    // For the text description improvement method
    public String mejorarDescripcion(String descripcion) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + geminiApiKey;
    
        // Construir el prompt para mejorar la redacción
        String prompt = "Como asistente de Urbia, mejora este reporte urbano. " +
                "Reescribe la descripción de manera clara y amigable (mínimo 40 caracteres, máximo 200). " +
                "Incluye: " +
                "1. Descripción del problema 📝 " +
                "2. Ubicación o zona afectada 📍 " +
                "3. Posible impacto 🔄 " +
                "Usa un tono cercano y añade 1-2 emojis relevantes. Recuerda que Urbia conecta a los vecinos " +
                "para mejorar la ciudad juntos. " +
                "Texto original: " + descripcion;
        
        // Construir el JSON de la solicitud
        JSONObject part = new JSONObject();
        part.put("text", prompt);
    
        JSONArray partsArray = new JSONArray();
        partsArray.put(part);
    
        JSONObject contentObject = new JSONObject();
        contentObject.put("parts", partsArray);
    
        JSONArray contentsArray = new JSONArray();
        contentsArray.put(contentObject);
    
        JSONObject payload = new JSONObject();
        payload.put("contents", contentsArray);
    
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    
        HttpEntity<String> requestEntity = new HttpEntity<>(payload.toString(), headers);
    
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
    
        if (response.getStatusCode() == HttpStatus.OK) {
            JSONObject jsonResponse = new JSONObject(response.getBody());
            // Verificar si existe el array "candidates"
            if (jsonResponse.has("candidates")) {
                JSONArray candidates = jsonResponse.getJSONArray("candidates");
                if (candidates.length() > 0) {
                    JSONObject candidate = candidates.getJSONObject(0);
                    // Intentar obtener el campo "output" si existe
                    if (candidate.has("output")) {
                        return candidate.getString("output");
                    } else if (candidate.has("content")) {
                        // El campo "content" es un objeto, extraerlo
                        JSONObject contentObj = candidate.getJSONObject("content");
                        if (contentObj.has("parts")) {
                            JSONArray parts = contentObj.getJSONArray("parts");
                            if (parts.length() > 0) {
                                JSONObject firstPart = parts.getJSONObject(0);
                                if (firstPart.has("text")) {
                                    return firstPart.getString("text");
                                }
                            }
                        }
                    }
                }
            }
        }
        // En caso de fallo, se retorna la descripción original
        return descripcion;
    }

    /**
     * Recupera una imagen pública a través de su URL, la codifica a Base64 y la envía a la API de Gemini
     * usando inline_data para obtener una respuesta generativa.
     *
     * @param imageUrl URL pública de la imagen.
     * @return El texto generado por Gemini o una cadena vacía en caso de error.
     */
    public String analizarImagenPublica(String imageUrl) {
        try {
            // Recuperar la imagen desde la URL
            ResponseEntity<byte[]> imageResponse = restTemplate.getForEntity(imageUrl, byte[].class);
            if (imageResponse.getStatusCode() != HttpStatus.OK || imageResponse.getBody() == null) {
                return "";
            }
            byte[] imageBytes = imageResponse.getBody();
            // Codificar la imagen a Base64 (sin saltos de línea)
            String encodedImage = Base64.getEncoder().encodeToString(imageBytes);
    
            // Determinar el MIME type según la extensión de la URL (puedes ajustar este mecanismo)
            String mimeType = "image/jpeg"; // valor por defecto
            if (imageUrl.endsWith(".png")) {
                mimeType = "image/png";
            } else if (imageUrl.endsWith(".gif")) {
                mimeType = "image/gif";
            }
    
            // Construir la parte de datos en línea (inline_data)
            JSONObject inlineData = new JSONObject();
            inlineData.put("mime_type", mimeType);
            inlineData.put("data", encodedImage);
    
            JSONObject inlinePart = new JSONObject();
            inlinePart.put("inline_data", inlineData);
    
            // Construir la parte de texto con la instrucción deseada
            JSONObject textPart = new JSONObject();
            textPart.put("text", "Analiza esta imagen para un reporte en Urbia 📱 " +
                "Describe de forma amigable (40-200 caracteres): " +
                "1. ¿Qué problema urbano se observa? 🔍 " +
                "2. ¿Cómo afecta al entorno? " +
                "3. Añade algún detalle relevante " +
                "Incluye 1-2 emojis relacionados. Recuerda que Urbia conecta vecinos para mejorar la ciudad juntos.");
    
            // Construir el arreglo de partes
            JSONArray partsArray = new JSONArray();
            partsArray.put(textPart);
            partsArray.put(inlinePart);
    
            JSONObject contentObject = new JSONObject();
            contentObject.put("parts", partsArray);
    
            JSONArray contentsArray = new JSONArray();
            contentsArray.put(contentObject);
    
            JSONObject payload = new JSONObject();
            payload.put("contents", contentsArray);
    
            // Usar el endpoint adecuado (en este ejemplo se usa gemini-1.5-flash)
            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + geminiApiKey;
    
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<>(payload.toString(), headers);
    
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
    
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                JSONObject jsonResponse = new JSONObject(response.getBody());
                if (jsonResponse.has("candidates")) {
                    JSONArray candidates = jsonResponse.getJSONArray("candidates");
                    if (candidates.length() > 0) {
                        JSONObject candidate = candidates.getJSONObject(0);
                        if (candidate.has("content")) {
                            JSONObject contentObj = candidate.getJSONObject("content");
                            if (contentObj.has("parts")) {
                                JSONArray parts = contentObj.getJSONArray("parts");
                                if (parts.length() > 0) {
                                    JSONObject firstPart = parts.getJSONObject(0);
                                    if (firstPart.has("text")) {
                                        return firstPart.getString("text");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return "";
    }



    public String analizarAudioPublica(String audioUrl) {
        try {
            // 1. Recuperar el archivo de audio desde la URL (por ejemplo, la URL pública de S3)
            URI uri = UriComponentsBuilder.fromHttpUrl(audioUrl).build(true).toUri();
            ResponseEntity<byte[]> audioResponse = restTemplate.getForEntity(uri, byte[].class);
            if (audioResponse.getStatusCode() != HttpStatus.OK || audioResponse.getBody() == null) {
                System.err.println("Error: No se pudo recuperar el audio desde " + audioUrl);
                return "";
            }
            byte[] audioBytes = audioResponse.getBody();
            int numBytes = audioBytes.length;
    
            // 2. Determinar el MIME type (según la extensión; se asume "audio/mp3" por defecto)
            String mimeType = "audio/mp3";
            String lowerUrl = audioUrl.toLowerCase();
            if (lowerUrl.endsWith(".wav")) {
                mimeType = "audio/wav";
            } else if (lowerUrl.endsWith(".ogg")) {
                mimeType = "audio/ogg";
            }
    
            // 3. Iniciar la sesión de carga usando la API de File
            String baseUrl = "https://generativelanguage.googleapis.com";
            String uploadStartUrl = baseUrl + "/upload/v1beta/files?key=" + geminiApiKey;
    
            HttpHeaders startHeaders = new HttpHeaders();
            startHeaders.set("X-Goog-Upload-Protocol", "resumable");
            startHeaders.set("X-Goog-Upload-Command", "start");
            startHeaders.set("X-Goog-Upload-Header-Content-Length", String.valueOf(numBytes));
            startHeaders.set("X-Goog-Upload-Header-Content-Type", mimeType);
            startHeaders.setContentType(MediaType.APPLICATION_JSON);
    
            // Payload de metadata para la carga (puedes ajustar el display_name según sea necesario)
            JSONObject startPayload = new JSONObject();
            JSONObject fileObj = new JSONObject();
            fileObj.put("display_name", "AUDIO");
            startPayload.put("file", fileObj);
    
            HttpEntity<String> startEntity = new HttpEntity<>(startPayload.toString(), startHeaders);
            ResponseEntity<String> startResponse = restTemplate.exchange(uploadStartUrl, HttpMethod.POST, startEntity, String.class);
            String uploadUrl = startResponse.getHeaders().getFirst("x-goog-upload-url");
            if (uploadUrl == null || uploadUrl.isEmpty()) {
                System.err.println("Error: No se recibió URL de carga.");
                return "";
            }
    
            // 4. Subir los bytes del archivo al URL de carga obtenido
            HttpHeaders uploadHeaders = new HttpHeaders();
            uploadHeaders.set("Content-Length", String.valueOf(numBytes));
            uploadHeaders.set("X-Goog-Upload-Offset", "0");
            uploadHeaders.set("X-Goog-Upload-Command", "upload, finalize");
    
            HttpEntity<byte[]> uploadEntity = new HttpEntity<>(audioBytes, uploadHeaders);
            ResponseEntity<String> uploadResponse = restTemplate.exchange(uploadUrl, HttpMethod.POST, uploadEntity, String.class);
            String uploadResponseBody = uploadResponse.getBody();
            if (uploadResponseBody == null) {
                System.err.println("Error: La respuesta de la carga es nula.");
                return "";
            }
            JSONObject uploadJson = new JSONObject(uploadResponseBody);
            String fileUri = "";
            if (uploadJson.has("file")) {
                JSONObject fileInfo = uploadJson.getJSONObject("file");
                if (fileInfo.has("uri")) {
                    fileUri = fileInfo.getString("uri");
                }
            }
            if (fileUri.isEmpty()) {
                System.err.println("Error: No se obtuvo file_uri en la respuesta de la carga.");
                return "";
            }
    
            // 5. Generar contenido usando el file_uri obtenido
            String generateUrl = baseUrl + "/v1beta/models/gemini-1.5-flash:generateContent?key=" + geminiApiKey;
            HttpHeaders generateHeaders = new HttpHeaders();
            generateHeaders.setContentType(MediaType.APPLICATION_JSON);
    
            // Construir el payload para generar contenido:
            // Se envía una parte de texto (prompt) y otra con file_data
            JSONObject textPart = new JSONObject();
            textPart.put("text", "Escucha este audio para un reporte en Urbia 🎧 " +
                "Proporciona una descripción amigable (40-200 caracteres) que incluya: " +
                "1. El problema mencionado 🔍 " +
                "2. La ubicación o zona " +
                "3. Algún detalle importante " +
                "Añade 1-2 emojis relevantes. Recuerda que Urbia conecta vecinos para mejorar la ciudad juntos.");
    
            JSONObject fileData = new JSONObject();
            fileData.put("mime_type", mimeType);
            fileData.put("file_uri", fileUri);
    
            JSONObject fileDataPart = new JSONObject();
            fileDataPart.put("file_data", fileData);
    
            JSONArray partsArray = new JSONArray();
            partsArray.put(textPart);
            partsArray.put(fileDataPart);
    
            JSONObject contentObject = new JSONObject();
            contentObject.put("parts", partsArray);
    
            JSONArray contentsArray = new JSONArray();
            contentsArray.put(contentObject);
    
            JSONObject generatePayload = new JSONObject();
            generatePayload.put("contents", contentsArray);
    
            HttpEntity<String> generateEntity = new HttpEntity<>(generatePayload.toString(), generateHeaders);
            ResponseEntity<String> generateResponse = restTemplate.exchange(generateUrl, HttpMethod.POST, generateEntity, String.class);
            if (generateResponse.getStatusCode() == HttpStatus.OK && generateResponse.getBody() != null) {
                JSONObject jsonResponse = new JSONObject(generateResponse.getBody());
                if (jsonResponse.has("candidates")) {
                    JSONArray candidates = jsonResponse.getJSONArray("candidates");
                    if (candidates.length() > 0) {
                        JSONObject candidate = candidates.getJSONObject(0);
                        if (candidate.has("content")) {
                            JSONObject contentObj = candidate.getJSONObject("content");
                            if (contentObj.has("parts")) {
                                JSONArray parts = contentObj.getJSONArray("parts");
                                if (parts.length() > 0) {
                                    JSONObject firstPart = parts.getJSONObject(0);
                                    if (firstPart.has("text")) {
                                        return firstPart.getString("text");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return "";
    }




}
