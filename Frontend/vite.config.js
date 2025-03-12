import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import path from "path";
import { fileURLToPath } from "url"; // Necesario para manejar rutas en ESM

// Obtener __dirname en ESM
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "src"), // Ahora `@` apunta a `src/`
    },
  },
  server: {
    port: 3000, // Servidor en puerto 3000
    open: true, // Abre el navegador automáticamente
    strictPort: true, // Evita que use otro puerto si el 3000 está ocupado
    historyApiFallback: true, // 🔹 Redirige todas las rutas a index.html
  },
  build: {
    outDir: "dist",
    sourcemap: true,
  },
  preview: {
    port: 4173, // Puerto de preview diferente para evitar conflictos
    historyApiFallback: true, // 🔹 También en preview
  },
});
