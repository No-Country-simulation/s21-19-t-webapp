import { useUserAuth } from "@/lib/store/useUserAuth";
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

export default function UserLogin() {
  const { user, login } = useUserAuth();

  if (user) return null; 

  return (
    <Dialog>
      <DialogTrigger className="bg-[#9bee5e] hover:bg-[#C8F79f] text-black w-[162px] h-[48px] rounded-2xl z-[9999]">
          Iniciar sesión
      </DialogTrigger>
    <DialogContent className="w-[900px] max-w-full h-[480px] z-[9999] flex rounded-2xl shadow-lg overflow-hidden p-0">
      <div className="w-1/2">
        <img src={loginImg} alt="Login" className="w-full h-full object-cover" />
      </div>
      <DialogHeader className="w-1/2">
        <div className="flex flex-col items-center justify-center pt-7 py-4">
          <img src={urbiLogin} alt="Urbi" className="w-[48px] h-[48px]" />
          <DialogTitle className="font-extrabold text-[24px]">Bienvenido otra vez!</DialogTitle>
        </div>
        <DialogDescription className="flex flex-col items-center justify-center">
          <section>
            <Label htmlFor="email" className="text-[#222222]" >Email</Label>
            <Input type="email" id="email" placeholder="Email" className="w-[370px] mt-2 mb-2" />
            <Label htmlFor="password" className="text-[#222222]">Contraseña</Label>
            <Input type="password" id="password" placeholder="Ingresa tu contraseña" className="w-[370px] mt-2" />
          </section>
          
            <section className="pt-4">
              <Button
                onClick={login} 
                className="bg-[#9bee5e] hover:bg-[#C8F79f] text-black w-[370px] h-[48px] rounded-[16px] z-[9999]"
              >
                Iniciar sesión
              </Button>
            <div className="py-2">
              <p className="text-center">O</p>
            </div>
              <Button 
                className="w-[370px] flex items-center justify-center border gap-2 border-gray-300 bg-white text-black hover:bg-gray-100 h-[48px] rounded-[16px]"
              >
                <img src={Google} alt="Google logo" className="w-[24px] h-[24px] " />
                Continuar con Google
              </Button>
              <p className="text-sm text-center mt-4 text-gray-600">
                ¿Aún no tienes cuenta? <a href="#" className="text-[#7E3AF2] font-medium">Regístrate</a>
              </p>
            </section>
          </DialogDescription>
        </DialogHeader>
      </DialogContent>
    </Dialog>

  );
}
