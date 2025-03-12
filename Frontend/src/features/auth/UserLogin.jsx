import { useUserAuth } from "@/lib/store/useUserAuth";
import { useState, useEffect } from "react";
import { Button } from "@/components/ui/button";
import { 
  Dialog, 
  DialogContent, 
  DialogDescription, 
  DialogHeader, 
  DialogTitle, 
  DialogTrigger 
} from "@/components/ui/dialog";
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";
import loginImg from "@/assets/Image.png";
import urbiLogin from "@/assets/svgs/urbiLogin.svg"
import Google from "@/assets/svgs/Google.svg"
import RegisterImg from "@/assets/ImageRegis.png"
import { toast } from "sonner";

export default function UserLogin({ isOpen, onOpenChange, isMobileMenu = false }) {
  const { user, login, register, loginWithGoogle } = useUserAuth();
  const [isMobile, setIsMobile] = useState(true);
  const [isLogin, setIsLogin] = useState(true);
  const [formData, setFormData] = useState({
    nombre: '',
    email: '',
    password: ''
  });
  const [isLoading, setIsLoading] = useState(false);

  // Verificar si estamos en móvil o desktop
  useEffect(() => {
    // Función para verificar el tamaño de la pantalla
    const checkScreenSize = () => {
      setIsMobile(window.innerWidth < 768);
    };
    
    // Verificar al montar
    checkScreenSize();
    
    // Agregar listener para cambios de tamaño
    window.addEventListener('resize', checkScreenSize);
    
    // Limpiar listener
    return () => window.removeEventListener('resize', checkScreenSize);
  }, []);

  // Si el usuario está logueado, no mostrar nada
  if (user) return null;
  
  // Si es para menú móvil y estamos en desktop, no mostrar nada
  if (isMobileMenu && !isMobile) return null;
  
  // Para uso independiente (ej. en header)
  const standalone = isOpen === undefined;

  // Manejar cambios en los inputs
  const handleInputChange = (e) => {
    const { id, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [id]: value
    }));
  };

  // Función para manejar el login
  const handleAuth = async () => {
    setIsLoading(true);
    try {
      if (isLogin) {
        await login(formData.email, formData.password);
        toast.success("¡Bienvenido de nuevo!", {
          description: "Has iniciado sesión correctamente"
        });
      } else {
        await register(formData.nombre, formData.email, formData.password);
        toast.success("¡Cuenta creada!", {
          description: "Tu cuenta ha sido creada correctamente"
        });
      }
      
      if (onOpenChange) {
        onOpenChange(false);
      }
    } catch (error) {
      toast.error(isLogin ? "Error al iniciar sesión" : "Error al registrarse", {
        description: error.message || "Ha ocurrido un error. Inténtalo de nuevo."
      });
    } finally {
      setIsLoading(false);
    }
  };

  // Manejador para el inicio de sesión con Google
  const handleGoogleLogin = (e) => {
    e.preventDefault();
    loginWithGoogle();
  };

  // Update the Google button in your return statement
  return (
    <Dialog>
      {standalone && (
        <DialogTrigger asChild>
          <Button className="bg-[#9bee5e] hover:bg-[#C8F79f] text-black w-[162px] h-[48px] rounded-2xl z-[9999] hidden md:block">
            Iniciar sesión
          </Button>
        </DialogTrigger>
      )}
      <DialogContent className="w-[900px] max-w-full h-auto z-[10000] flex rounded-2xl shadow-lg overflow-hidden p-0">
        <div className="w-1/2 hidden md:block">
          <img src={isLogin ? loginImg : RegisterImg} alt={isLogin ? "Login image" : "Register image"} className="w-full h-full object-cover" />
        </div>
        <DialogHeader className="w-full md:w-1/2">
          <div className="flex flex-col items-center justify-center pt-7 py-4">
            <img src={urbiLogin} alt="Urbi" className="w-[48px] h-[48px]" />
            <DialogTitle className="font-extrabold text-[24px]">
            {isLogin ? "Bienvenido otra vez!" : "Únete a Urbia"}
            </DialogTitle>
          </div>
          <DialogDescription className=" flex flex-col items-center justify-center px-6">
            <section className="w-full px-4 md:px-0">
              {!isLogin && (
                <div>
                  <Label htmlFor="nombre" className="text-[#222222]">Nombre</Label>
                  <Input 
                    type="text" 
                    id="nombre" 
                    placeholder="Nombre completo" 
                    className="w-full md:w-[370px] mt-2 mb-2"
                    value={formData.nombre}
                    onChange={handleInputChange}
                  />
                </div>
              )}
              <Label htmlFor="email" className="text-[#222222]">Email</Label>
              <Input 
                type="email" 
                id="email" 
                placeholder="Email" 
                className="w-full md:w-[370px] mt-2 mb-2"
                value={formData.email}
                onChange={handleInputChange}
              />
              <Label htmlFor="password" className="text-[#222222]">Contraseña</Label>
              <Input 
                type="password" 
                id="password" 
                placeholder="Ingresa tu contraseña" 
                className="w-full md:w-[370px] mt-2"
                value={formData.password}
                onChange={handleInputChange}
              />
            </section>
            
            <section className="pt-4 w-full px-4 md:px-0">
              <Button
                onClick={handleAuth} 
                className="bg-[#9bee5e] hover:bg-[#C8F79f] text-black w-full md:w-[370px] h-[48px] rounded-[16px] z-[9999]"
                disabled={isLoading}
              >
                {isLoading ? "Procesando..." : (isLogin ? "Iniciar sesión" : "Registrarse")}
              </Button>
              <div className="py-2">
              <p className="text-center">O</p>
                </div>
                <Button 
                  onClick={handleGoogleLogin}
                  className="w-full md:w-[370px] flex items-center justify-center border gap-2 border-gray-300 bg-white text-black hover:bg-gray-100 h-[48px] rounded-[16px]"
                >
                  <img src={Google} alt="Google logo" className="w-[24px] h-[24px]" />
                  {isLogin ? "Continuar con Google" : "Registrarse con Google"}
                </Button>
                <div className="text-sm text-center mt-4 text-gray-600">
                <p className="text-sm text-center mt-3 text-gray-600">
                {isLogin ? "¿Aún no tienes cuenta?" : "¿Ya tienes una cuenta?"}{" "}
                <button 
                  onClick={() => setIsLogin(!isLogin)} 
                  className="text-[#7E3AF2] font-medium"
                >
                  {isLogin ? "Regístrate" : "Inicia sesión"}
                </button>
              </p>
              </div>
            </section>
          </DialogDescription>
        </DialogHeader>
      </DialogContent>
    </Dialog>
  );
}
