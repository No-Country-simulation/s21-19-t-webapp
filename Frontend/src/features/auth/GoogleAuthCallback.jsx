import { useEffect, useState } from "react";
import {  useLocation } from "react-router-dom";
import { useUserAuth } from "@/lib/store/useUserAuth";
import { toast } from "sonner";
import { ErrorMessage } from "@/components/ui/ErrorMessage";
import sadFrog from "@/assets/frogError.png";

export default function GoogleAuthCallback() {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const location = useLocation();
  const { handleGoogleCallback } = useUserAuth();

  useEffect(() => {
    const processCallback = async () => {
      console.log("✅ GoogleAuthCallback montado");

      const params = new URLSearchParams(location.search);
      const token = params.get("token");

      console.log("🔑 Token recibido:", token);

      if (!token) {
        console.error("❌ No se recibió el token en la URL");
        setError("No se recibió el token de autenticación.");
        setLoading(false);
        return;
      }

      try {
        const success = handleGoogleCallback(token);
        if (success) {
          toast.success("¡Bienvenido a Urbia!", {
            description: "Has iniciado sesión correctamente con Google",
          });

          console.log("🔄 Redirigiendo al home...");
          setTimeout(() => {
            window.location.href = "/"; // 🔹 Redirección más robusta
          }, 1000);
        } else {
          throw new Error("Error procesando el token de autenticación.");
        }
      } catch (err) {
        console.error("❌ Error al procesar el token:", err);
        setError(err.message || "Error durante la autenticación.");
      } finally {
        setLoading(false);
      }
    };

    processCallback();
  }, [location, handleGoogleCallback]);

  if (loading) {
    return (
      <div className="flex items-center justify-center h-screen">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-gray-900 mx-auto mb-4"></div>
          <p className="text-lg font-medium">Procesando tu inicio de sesión...</p>
        </div>
      </div>
    );
  }

  if (error) {
    return <ErrorMessage message={`Error al iniciar sesión: ${error}`} imageSrc={sadFrog} />;
  }

  return null;
}
