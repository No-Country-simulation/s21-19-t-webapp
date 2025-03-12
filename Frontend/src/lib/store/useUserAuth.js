import { create } from "zustand";
import { persist } from "zustand/middleware";
import { login as loginRequest } from "../api/auth/login";
import { register as registerRequest } from "../api/auth/register";
import { logout as logoutRequest } from "../api/auth/logout";
import {jwtDecode}  from "jwt-decode"; // You'll need to install this package

export const useUserAuth = create(
  persist(
    (set) => ({
      user: null,
      setUser: (userData) => {
        set({ user: userData });
      },
      loginWithGoogle: () => {
        // Determinar la URL de redirección basada en el entorno
        const isProduction = window.location.hostname !== 'localhost';
        
        // Usar la URL completa para la redirección
        const frontendBaseUrl = isProduction 
          ? 'https://urbia.onrender.com'
          : 'http://localhost:3000';
        
        // Redirigir a la URL de autenticación de Google con el endpoint correcto
        window.location.href = `https://api-urbia.up.railway.app/oauth2/authorization/google?redirect_uri=${encodeURIComponent(frontendBaseUrl + '/auth/callback')}`;
      },
      // Add this new method to handle the callback
      handleGoogleCallback: (token) => {
        if (token) {
          try {
            // Decode the JWT token
            const decodedToken = jwtDecode(token);
            
            // Save token to localStorage
            localStorage.setItem('token', token);
            
            // Set user data from decoded token
            set({
              user: {
                id: decodedToken.sub || decodedToken.id,
                name: decodedToken.name || decodedToken.nombre,
                email: decodedToken.email,
                avatar: decodedToken.avatar || null,
              },
            });
            
            return true;
          } catch (error) {
            console.error("Error decoding token:", error);
            return false;
          }
        }
        return false;
      },
      login: async (email, password) => {
        try {
          const userData = await loginRequest(email, password);
          set({
            user: {
              id: userData.id,
              name: userData.nombre,
              email: userData.email,
              avatar: userData.avatar || null,
            },
          });
          return userData;
        } catch (error) {
          console.error("❌ Error al iniciar sesión:", error);
          throw error;
        }
      },
      register: async (nombre, email, password) => {
        try {
          const userData = await registerRequest(nombre, email, password);
          set({
            user: {
              id: userData.id,
              name: userData.nombre,
              email: userData.email,
              avatar: userData.avatar || null,
            },
          });
          return userData;
        } catch (error) {
          console.error("❌ Error al registrar usuario:", error);
          throw error;
        }
      },
      logout: async () => {
        try {
          await logoutRequest();
          set({ user: null });
        } catch (error) {
          console.error("❌ Error al cerrar sesión:", error);
          // Even if the API call fails, we still clear the local user data
          set({ user: null });
        }
      },
    }),
    {
      name: "user-auth", // Nombre de la key en localStorage
      getStorage: () => localStorage, // Usa localStorage
    }
  )
);
