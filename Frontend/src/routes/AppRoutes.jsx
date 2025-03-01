import { Routes, Route, BrowserRouter } from 'react-router-dom';
import HomePage from '@/features/home/pages/HomePage';
import Menu from '@/components/layout/Menu';
import NewReportForm from '@/features/NewReportForm/NewReportForm';

export default function AppRoutes() {
  return (
    <BrowserRouter>
    <div className='absolute z-[9999] left-72'>
      <Menu />

      <NewReportForm />

    </div>
      <Routes>
        <Route path='/' element={<HomePage />} />
        {/* Otras rutas si las hay */}
      </Routes>
    </BrowserRouter>
  );
}
