// features/home/pages/HomePage.jsx
import { useEffect } from 'react';
import useReportsStore from '../../../lib/store/useReportsStore';
import MapView from '@/components/mapview/MapView';
import { useWebSocketReports } from '@/components/mapview/hooks/useWebSocketReports';

function HomePage() {
  const { reports, fetchReports } = useReportsStore();
  
  // Initialize WebSocket connection at the HomePage level
  useWebSocketReports();

  useEffect(() => {
    console.log('HomePage mounted, fetching initial reports...');
    // Usar true para forzar la actualización y evitar problemas de caché
    fetchReports(true).then(() => {
      console.log('Initial reports fetched successfully');
    }).catch(error => {
      console.error('Error fetching initial reports:', error);
    });
    
  }, [fetchReports]);

  return (
    <div className='h-screen w-full'>
      <MapView reports={Array.isArray(reports) ? reports : []} />  
    </div>
  );
}

export default HomePage;
