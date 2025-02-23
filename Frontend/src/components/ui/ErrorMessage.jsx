// ErrorMessage.jsx
import { useState, useEffect } from 'react';

export function ErrorMessage({ message, imageSrc }) {
  const [visible, setVisible] = useState(true);

  useEffect(() => {
    const timer = setTimeout(() => setVisible(false), 6000);
    return () => clearTimeout(timer);
  }, []);

  if (!visible) return null;

  return (
    <div className="absolute inset-0 flex flex-col items-center justify-start z-[9999]">
      {imageSrc && <img src={imageSrc} alt="Error" className="mb-4 h-24" />}
      <div className="bg-green-950 text-white p-4 rounded shadow-md">
        {message}
      </div>
    </div>
  );
}
