import axios from "axios";

const api = axios.create({
  baseURL: "https://s21-19-t-webapp-o3vj.onrender.com",
  timeout: 15000,
  headers: {
    "Content-Type": "application/json",
  },
});

export default api;
