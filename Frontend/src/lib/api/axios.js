import axios from "axios";

const api = axios.create({
  baseURL: "https://s21-19-t-webapp-pw5h.onrender.com",
  timeout: 15000,
  headers: {
    "Content-Type": "application/json",
  },
});

// Check for existing token on initialization
const token = localStorage.getItem('token');
if (token) {
  api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
}

// Add response interceptor for debugging
api.interceptors.request.use(
  (config) => {
    console.log("🚀 Request:", config.method.toUpperCase(), config.url);
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default api;
