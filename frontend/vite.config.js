import {defineConfig} from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
    plugins: [react()],
    server: {
        port: 5173,
        proxy: {
            "/api/auth": {
                target: "http://localhost:8085",
                changeOrigin: true
            },
            "/api/v1/courses": {
                target: "http://localhost:8082",
                changeOrigin: true
            },
            "/api/v1/enrollments": {
                target: "http://localhost:8083",
                changeOrigin: true
            },
            "/api/payments": {
                target: "http://localhost:8086",
                changeOrigin: true
            },
            "/api/documents": {
                target: "http://localhost:8087",
                changeOrigin: true
            }
        }
    }
});