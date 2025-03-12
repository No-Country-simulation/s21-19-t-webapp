import { useEffect } from 'react';
import { useUserAuth } from '@/lib/store/useUserAuth';
import api from '@/lib/api/axios';

export default function AuthCallback() {
  const { setUser } = useUserAuth();

  useEffect(() => {
    const processOAuthCallback = async () => {
      try {
        // Extract token from URL
        const params = new URLSearchParams(window.location.search);
        const token = params.get('token');
        
        if (!token) {
          throw new Error('No token received');
        }
        
        // Store token in localStorage
        localStorage.setItem('token', token);
        
        // Set token in axios headers
        api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        
        // Decode the JWT token to get user info
        const tokenParts = token.split('.');
        if (tokenParts.length === 3) {
          try {
            const payload = JSON.parse(atob(tokenParts[1]));
            
            // Update user state with info from token
            setUser({
              id: payload.sub || payload.id,
              email: payload.sub || payload.email,
              name: payload.name || payload.sub.split('@')[0],
              avatar: payload.avatar || null,
            });
            
            console.log('User authenticated successfully');
          } catch (e) {
            console.error('Error decoding token:', e);
          }
        }
        
        // Determine redirect URL based on environment
        const isProduction = window.location.hostname !== 'localhost';
        const redirectUrl = isProduction 
          ? 'https://urbia.onrender.com/' 
          : 'http://localhost:3000/';
        
        // Redirect to home page
        window.location.href = redirectUrl;
      } catch (error) {
        console.error('Error processing OAuth callback:', error);
        
        // Even on error, redirect to home page
        const isProduction = window.location.hostname !== 'localhost';
        const redirectUrl = isProduction 
          ? 'https://urbia.onrender.com/' 
          : 'http://localhost:3000/';
        
        window.location.href = redirectUrl;
      }
    };

    processOAuthCallback();
  }, [setUser]);

  return (
    <div className="flex items-center justify-center min-h-screen">
      <div className="text-center">
        <p className="text-lg font-medium">Procesando inicio de sesión...</p>
        <p className="text-sm text-gray-500 mt-2">Serás redirigido automáticamente</p>
      </div>
    </div>
  );
}