import api from "../axios";

export const postReport = async (formData) => {
  try {
    const response = await api.post("/api/reporte/combinado", formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
    console.log("✅ Reporte enviado:", response.data);
    return response.data;
  } catch (error) {
    console.error("❌ Error al enviar reporte:", error);
    throw error;
  }
};
