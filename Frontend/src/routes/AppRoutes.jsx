import { Routes, Route, BrowserRouter, useLocation } from 'react-router-dom';
import HomePage from '@/features/home/pages/HomePage';
import Menu from '@/components/layout/Menu';
import CleanReportForm from '@/features/reports/form/CleanReportForm';
import { Toaster } from '@/components/ui/sonner';
import GoogleAuthCallback from '@/features/auth/GoogleAuthCallback';

function RouteLogger() {
  const location = useLocation();
  console.log("📍 Navegando a:", location.pathname);
  return null;
}


export default function AppRoutes() {
  return (
    <BrowserRouter>
          <RouteLogger />
      <div className='absolute z-[9999]'>
        <Menu />
        {/* Keep the form component, but it won't show a button anymore */}
        <CleanReportForm />
        <Toaster />
      </div>
      <Routes>
      <Route path="/auth/callback" element={<GoogleAuthCallback />} />
        <Route path='/' element={<HomePage />} />
        {/* Otras rutas si las hay */}
      </Routes>
    </BrowserRouter>
  );
}
