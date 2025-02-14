import { MapContainer, TileLayer, Marker, Popup } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import PropTypes from 'prop-types';

const MapView = ({ reports }) => {
  const defaultPosition = [ -34.6037, -58.3816 ]; // Ubicación por defecto (Ej: Buenos Aires)

  return (
    <MapContainer center={defaultPosition} zoom={13} className="h-96 w-full rounded-lg">
      {/* Capa base de OpenStreetMap */}
      <TileLayer
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
      />
      
      {/* Marcadores dinámicos */}
      {reports.map((report, index) => (
        <Marker key={index} position={[report.lat, report.lng]}>
          <Popup>
            <strong>{report.title}</strong> <br />
            {report.description}
          </Popup>
        </Marker>
      ))}
    </MapContainer>
  );
};
MapView.propTypes = {
  reports: PropTypes.arrayOf(
    PropTypes.shape({
      lat: PropTypes.number.isRequired,
      lng: PropTypes.number.isRequired,
      title: PropTypes.string.isRequired,
      description: PropTypes.string.isRequired,
    })
  ).isRequired,
};

export default MapView;

