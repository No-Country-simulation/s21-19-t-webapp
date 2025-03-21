# 🤝 Guía de Contribución - Urbia

¡Gracias por tu interés en contribuir a **Urbia**! 🎉 Este documento describe las reglas y mejores prácticas para colaborar en el proyecto.

---

## 📌 Cómo Contribuir

### 🚀 1. Requisitos Previos
Antes de contribuir, asegúrate de tener:
- Node.js y npm instalados.
- Java JDK 17+.
- PostgreSQL.
- Git configurado.
- Un entorno de desarrollo adecuado (VS Code recomendado).

---

### 🔀 2. Flujo de Trabajo en Git

1. **Fork** el repositorio y clónalo en tu máquina.
2. **Crea una rama** basada en `dev`:
   ```sh
   git checkout dev
   git pull origin dev
   git checkout -b feature/nueva-funcionalidad
   ```
3. **Realiza cambios**, sigue las convenciones de código y haz commits estructurados.
4. **Haz push de tu rama** y crea un Pull Request a `dev`.
5. **Espera revisión** y realiza los cambios sugeridos si es necesario.
6. **Una vez aprobado, se mergea a `dev`** y se elimina la rama.

---

### 🎯 3. Convención de Commits

Cada commit debe incluir un **emoji** y una descripción clara en español:

| Tipo                | Emoji | Ejemplo de Commit |
|--------------------|--------|-------------------------------|
| **Nueva funcionalidad** | 🚀 `:construction:` | `🚀 Agregar nueva funcionalidad de reportes` |
| **Corrección de errores** | 🐛 `:bug:` | `🐛 Corregir error en la carga de imágenes` |
| **Refactorización** | ♻️ `:recycle:` | `♻️ Refactorizar la lógica de autenticación` |
| **Documentación** | 📝 `:memo:` | `📝 Actualizar README con guía de instalación` |

---

### 🛠 4. Estilo y Formato de Código

- Usamos **Prettier y ESLint** para el formateo automático.
- Código limpio siguiendo principios **SOLID**.
- Usa **nombres descriptivos** para variables y funciones.
- Evita código comentado innecesario.

Ejecuta antes de hacer commit:
```sh
npm run lint
npm run format
```

---

### ✅ 5. Pruebas y Validaciones
Antes de hacer un Pull Request:
- Asegúrate de que el código no rompe otras funcionalidades.
- Ejecuta pruebas unitarias y de integración.
- Confirma que no haya errores de linting ni warnings.

Ejemplo:
```sh
npm test
```

---

### 🗣 6. Comunicación
Si tienes dudas o sugerencias, únete a nuestro canal de **Discord** o abre un **Issue** en GitHub.

📢 **¡Tu contribución ayuda a mejorar Urbia! 🚀**
