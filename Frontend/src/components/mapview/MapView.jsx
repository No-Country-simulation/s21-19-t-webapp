import { useEffect, useState, useRef } from 'react';
import { MapContainer, TileLayer, ZoomControl, Marker } from 'react-leaflet';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import { Toaster } from 'sonner';

import { useUserLocation } from './hooks/useUserLocation';
// Keep the useReverseGeocode import as it might be used elsewhere
import { useUserAuth } from '@/lib/store/useUserAuth';
import { useCities } from './hooks/useCities';
import { getGeolocationErrorMessage } from '@/lib/utils/errorMessages';
import useMapStore from '@/lib/store/useMapStore';
import useReportsStore from '@/lib/store/useReportsStore';

import ReportMarker from './ReportMarker';
import Recenter from './Recenter';
import MyLocationButton from './MyLocationButton';
import MapClickHandler from './useMapClick';
import ReportView from '@/features/reports/ReportView';
import CitySelectionDialog from './CitySelectionDialog';
import CityNavigation from './CityNavigation';
import UserMenu from '@/features/auth/MenuUser';
import UserLogin from '@/features/auth/UserLogin';
import InstallPWAButton from './AddPWAButton';
// Remove the AddressCard import
// import { AddressCard } from "../Adress/AdressCard";
import CleanReportForm from '@/features/reports/form/CleanReportForm';
import AdCarousel from './AdCarousel';

import userIcon from '/frogIco.png';
import reportIcon from '@/assets/nenufarIco.png';
import sadFrog from '@/assets/frogError.png';

import './style/MapView.css';
import { AnimatedCircle } from './AnimatedCircle';

import ButtonAddNewReport from '@/features/reports/form/components/ButtonAddNewReport';

const wazeIcon = L.icon({
  iconUrl: userIcon,
  iconSize: [30, 30],
  iconAnchor: [20, 20],
  popupAnchor: [0, -20]
});

// Create a new icon for the click marker using the same frog icon
const clickMarkerIcon = L.icon({
  iconUrl: reportIcon,
  iconSize: [35, 35],
  iconAnchor: [17, 35],
  popupAnchor: [0, -35]
});

export default function MapView() {
  const { groupedReports, deleteReport } = useReportsStore(); // Add deleteReport here

  const { center, position, error, loading, geolocationStatus } =
    useUserLocation([-34.6037, -58.3816]);
  const defaultZoom = 18;
  const [map, setMap] = useState(null);

  const { user } = useUserAuth();
  const { cities } = useCities();
  const [modalOpen, setModalOpen] = useState(false);
  const modalHasBeenOpened = useRef(false);

  const { selectedCoords, loadStoredCoords } = useMapStore();

  // Estado para manejar el reporte seleccionado y abrir ReportView
  const [selectedReport, setSelectedReport] = useState(null);

  useEffect(() => {
    loadStoredCoords();
  }, [loadStoredCoords]);

  useEffect(() => {
    if (
      !loading &&
      ['browser_denied', 'ip_error_default', 'timeout_default'].includes(
        geolocationStatus
      ) &&
      !modalHasBeenOpened.current
    ) {
      setModalOpen(true);
      modalHasBeenOpened.current = true;
    }
  }, [loading, geolocationStatus]);

  const handleCitySelect = cityName => {
    const city = cities.find(c => c.name === cityName);
    if (city && map) {
      map.setView([city.lat, city.lng], defaultZoom);
      setModalOpen(false);
    }
  };

  const errorMessage = getGeolocationErrorMessage(geolocationStatus, error);

  // Function to handle report selection and center the map
  const handleReportSelect = report => {
    setSelectedReport(report);

    // Use flyTo for a smoother animation when centering on the report
    if (map && report) {
      map.flyTo([report.latitud, report.longitud], defaultZoom, {
        duration: 1.5, // Animation duration in seconds
        easeLinearity: 0.25 // Makes the animation more natural
      });
    }
  };

  return (
    <div className='relative w-full h-screen'>
      <Toaster
        position='bottom-center'
        toastOptions={{
          style: {
            background: 'white',
            color: 'black',
            borderRadius: '12px',
            boxShadow: '0 4px 12px rgba(0, 0, 0, 0.1)',
            padding: '16px',
            width: 'min(90vw, 600px)',
            maxWidth: '600px',
            left: '50%',
            transform: 'translateX(-50%)',
            margin: '0 auto'
          }
        }}
      />

      {/* Ad Carousel - positioned at the top of the map */}
      <div className="absolute bottom-5 left-44 transform -translate-x-1/2 z-[999] w-[90%] max-w-[300px] max-h-[70px]">
        <AdCarousel />
      </div>

      <MapContainer
        center={center}
        zoom={defaultZoom}
        className='w-full h-full'
        zoomControl={false}
        whenReady={event => setMap(event.target)}
        minZoom={4}
      >
        <TileLayer
          url='https://{s}.basemaps.cartocdn.com/rastertiles/voyager/{z}/{x}/{y}{r}.png'
          maxZoom={20}
        />

        <ZoomControl position='bottomright' />
        <Recenter center={center} zoom={defaultZoom} />
        <MapClickHandler />

        {/* Marcador ubicación del usuario */}
        {!error && position && (
          <>
            <Marker position={position} icon={wazeIcon} />
            <AnimatedCircle
              center={position}
              initialRadius={40}
              maxRadius={80}
              duration={4000} // Duración en milisegundos del ciclo de expansión
              pathOptions={{
                color: 'blue',
                fillColor: 'blue',
                stroke: false
              }}
            />
          </>
        )}

        {/* Marcador de click en el mapa - Updated to use the frog icon */}
        {selectedCoords && (
          <Marker position={selectedCoords} icon={clickMarkerIcon} />
        )}

        {/* Render de reportes agrupados */}
        {Object.values(groupedReports).map((group, index) => {
          if (!Array.isArray(group) || group.length === 0) return null;

          return (
            <ReportMarker
              key={index}
              reports={group}
              // Update to use the new handler function
              onReportSelect={handleReportSelect}
            />
          );
        })}
      </MapContainer>

      <MyLocationButton
        map={map}
        position={position}
        defaultZoom={defaultZoom}
        className='absolute bottom-10 lg:bottom-32 right-7 z-[9999]'
      />

      {/* Add the ButtonAddNewReport component here */}
      {user && <ButtonAddNewReport />}

      {/* Remove the AddressCard component */}
      {/* {position && (address || loadingAddress || addressError) && (
        <div className="hidden md:block overflow-hidden absolute bottom-5 left-20 z-[9999]">
          <AddressCard
            address={address}
            loadingAddress={loadingAddress}
            addressError={addressError}
          />
        </div>
      )} */}

      <div className='absolute top-5 right-5 z-[9999] flex items-center gap-4'>
        <InstallPWAButton />
        <div className='hidden lg:flex'>
          {user ? <UserMenu /> : <UserLogin />}
        </div>
      </div>

      {modalOpen && (
        <CitySelectionDialog
          open={modalOpen}
          onClose={() => setModalOpen(false)}
          cities={cities}
          map={map}
          onCitySelect={handleCitySelect}
          message=''
          errorMessage={errorMessage}
          errorImage={sadFrog}
        />
      )}

      <CityNavigation map={map} />

      {/* Hidden button to trigger the report form */}
      <button id='open-report-form' className='hidden' aria-hidden='true' />

      {/* ReportView se abre cuando seleccionamos un reporte */}
      {selectedReport && (
        <ReportView
          report={selectedReport}
          onClose={() => setSelectedReport(null)}
          deleteReport={deleteReport} // Pass the deleteReport function here
        />
      )}

      {/* Clean Report Form */}
      <CleanReportForm />
    </div>
  );
}
