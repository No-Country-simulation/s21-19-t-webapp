import { Routes, Route, BrowserRouter } from 'react-router-dom';
import HomePage from '@/features/home/pages/HomePage';
import Menu from '@/components/layout/Menu';
import CleanReportForm from '@/features/reports/form/CleanReportForm';
import { Toaster } from '@/components/ui/sonner';
import AuthCallback from '@/features/auth/AuthCallback';

export default function AppRoutes() {
  return (
    <BrowserRouter>
      <div className='absolute z-[9999]'>
        <Menu />
        {/* Keep the form component, but it won't show a button anymore */}
        <CleanReportForm />
        <Toaster />
      </div>
      <Routes>
        <Route path="/auth/callback" element={<AuthCallback />} />
        <Route path='/' element={<HomePage />} />
        {/* Otras rutas si las hay */}
      </Routes>
    </BrowserRouter>
  );
}
