import { Marker, Popup } from 'react-leaflet';
import PropTypes from 'prop-types';
import L from 'leaflet';

// Íconos para cada categoría
import infraIcon from '@/assets/svgs/FrogPinInfra.svg';
import seguridadIcon from '@/assets/svgs/FrogPinPoli.svg';
import saludIcon from '@/assets/svgs/FrogPinSalud.svg';
import eventosIcon from '@/assets/svgs/FrogPinSocial.svg';

const categoryIcons = {
  1: infraIcon,
  2: seguridadIcon,
  3: saludIcon,
  4: eventosIcon
};

export default function ReportMarker({ report }) {
  const iconUrl = categoryIcons[report.categoriaId] || infraIcon;


  const customMarker = L.icon({
    iconUrl,
    iconSize: [50, 50],
    iconAnchor: [20, 50],
    popupAnchor: [0, -40]
  });

  return (
    <Marker position={[report.latitud, report.longitud]} icon={customMarker}>
      <Popup>
        <strong>{report.titulo}</strong>
        <br />
        {report.descripcion}
      </Popup>
    </Marker>
  );
}

ReportMarker.propTypes = {
  report: PropTypes.shape({
    latitud: PropTypes.number.isRequired,
    longitud: PropTypes.number.isRequired,
    categoriaID: PropTypes.number.isRequired,
    titulo: PropTypes.string.isRequired,
    descripcion: PropTypes.string.isRequired
  }).isRequired
};
