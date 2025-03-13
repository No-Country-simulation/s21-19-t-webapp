import { useState, useEffect } from "react";
import { Card, CardContent } from "@/components/ui/card";
import { cn } from "@/lib/utils";
import { motion, AnimatePresence } from "framer-motion";

// Import your ad images
import mercadoLivreImg from "@/assets/ads/MLPubli.png";
import noCountryImg from "@/assets/ads/noCountryPubli.png";
import wazeImg from "@/assets/ads/Waze2.png";

const ads = [
  {
    id: 1,
    image: mercadoLivreImg,
    alt: "Mercado Libre",
    url: "https://www.mercadolibre.com",
  },
  {
    id: 2,
    image: noCountryImg,
    alt: "No Country",
    url: "https://www.nocountry.tech",
  },
  {
    id: 3,
    image: wazeImg,
    alt: "Waze",
    url: "https://www.waze.com/es/live-map/",
  },
  // Add more ads as needed
];

export default function AdCarousel({ className }) {
  const [currentIndex, setCurrentIndex] = useState(0);
  
  // Auto-rotate ads every 5 seconds
  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentIndex((prevIndex) => (prevIndex + 1) % ads.length);
    }, 6000);
    
    return () => clearInterval(interval);
  }, []);

  const handleAdClick = (url) => {
    window.open(url, "_blank", "noopener,noreferrer");
  };

  return (
    <Card className={cn("w-full overflow-hidden border-1 shadow-2xl  rounded-lg", className)}>
      <CardContent className="p-0">
        <AnimatePresence mode="wait">
          <motion.div
            key={currentIndex}
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            transition={{ duration: 0.5 }}
            className="relative w-[200px] h-[50px] md:w-[300px] md:h-[80px] cursor-pointer"
            onClick={() => handleAdClick(ads[currentIndex].url)}
          >
            <img 
              src={ads[currentIndex].image} 
              alt={ads[currentIndex].alt}
              className="w-full h-full object-contain rounded-lg"
            />
          </motion.div>
        </AnimatePresence>
        
        {/* Indicator dots - made smaller and more subtle */}
{/*         <div className="absolute bottom-1 left-0 right-0 flex justify-center gap-1">
          {ads.map((_, index) => (
            <div 
              key={index}
              className={cn(
                "w-1.5 h-1.5 rounded-full transition-all duration-400",
                index === currentIndex ? "bg-white scale-110" : "bg-white/50"
              )}
            />
          ))}
        </div> */}
      </CardContent>
    </Card>
  );
}