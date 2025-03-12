import { useEffect, useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { useUserAuth } from '@/lib/store/useUserAuth';
import { toast } from 'sonner';
import { ErrorMessage } from '@/components/ui/ErrorMessage';
import sadFrog from '@/assets/frogError.png';

export default function GoogleAuthCallback() {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();
  const location = useLocation();
  const { handleGoogleCallback } = useUserAuth();

  useEffect(() => {
    const processCallback = async () => {
      try {
        console.log("✅ GoogleAuthCallback montado");
        // Extract token from URL query parameters
        const params = new URLSearchParams(location.search);
        const token = params.get('token');
        console.log("Token recibido:", token);

        if (!token) {
          throw new Error('No token received from authentication server');
        }

        // Process the token in our auth store
        const success = handleGoogleCallback(token);
        
        if (success) {
          toast.success('¡Bienvenido a Urbia!', {
            description: 'Has iniciado sesión correctamente con Google'
          });
          
          // Redirect to home page after successful login
          navigate('/');
        } else {
          throw new Error('Error processing authentication token');
        }
      } catch (err) {
        console.error('Authentication callback error:', err);
        setError(err.message || 'Error during authentication');
      } finally {
        setLoading(false);
      }
    };

    processCallback();
  }, [location, handleGoogleCallback, navigate]);

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

  return null; // This component doesn't render anything on success (redirects instead)
}