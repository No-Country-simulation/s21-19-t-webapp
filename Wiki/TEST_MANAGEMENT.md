# 📚 Gestión de Pruebas "Urbia"

La calidad es uno de los factores importantes y determinantes para la creación de aplicaciones, es porq ello que este espacio abordaremos como se llevará a cabo la planificación de pruebas para la App Urbia.

## 📂 Documentación

Se ha construido un Plan de Pruebas el cual actualizará a lo largo del desarrollo.

| Versión | Descripción | Documento
|---------|------------|------------|
| v1.0.0 | Inicio de documentación de pruebas, planificación de y definición de alcance, antecedentes, objetos y selección de pruebas dentro del ámbito y fuera del ámbito | [`Plan de Pruebas Proyecto Urbia v1.0.0`](https://docs.google.com/document/d/1TnDLdPWSCnrIitmXT4zv-Jm77ynOHzYO/edit?usp=drive_link&ouid=105679593414464607056&rtpof=true&sd=true) |
| v1.0.1 | Se integró en Enfoque de las Pruebas, Herramientas utilizadas para las pruebas y los casos de prueba iniciales. | [`Plan de Pruebas Proyecto Urbia v1.0.1`](https://docs.google.com/document/d/1eXsgJv3RY6L9ttA0VSP-ZYbvmyVxT42E/edit?usp=sharing&ouid=105679593414464607056&rtpof=true&sd=true) |
| v1.0.2 | Incorporación de nuevos casos de prueba y establecimiento de riesgos, reporte de problemas y solución, así como sección de responsabilidades, personal y necesidades de capacitación. | [`Plan de Pruebas Proyecto Urbia v1.0.2`](https://docs.google.com/document/d/1bVrNkWEaicMixDMYYhdaumh3ESZsF6y-/edit?usp=sharing&ouid=105679593414464607056&rtpof=true&sd=true) |

## 🚀 ¿Cómo se llevará el control de las pruebas con el equipo de desarrollo?

1. **Plan de Trabajo:** se definio un plan de trabajo de testing ágil de tal forma que el equipo de QA puede identificar cuales son las tareas que estan listas para probar. Para ello se definio un tablero Kanban con columnas estratégicas (To Code, To Design, In progress, In Review, In Testing, Done). La dinamica del plan de trabajo para las pruebas consiste en que cuando el desarrollador termine una tareas "In Progress" pueda posicionarla en la columna "In Review" lo cual indicará al equipo de QA que esa actividad se encuentra lista para Testear, el equipo de QA la toma y pasa a la columna de "In Testing" para indicar que ya se encuentra probando la actividad, en caso que no presente detalles la pasa a la columna Done, en caso que presente detalles la devuelve a la columna "In Progress" y notifica las correcciones en tablero Bug's, si son correcciones muy mínimas se podrá reportar en la misma tarea. 

![image](https://github.com/user-attachments/assets/87da9504-5fd0-4d3d-a3aa-86544be4e733)

2. **Estructura de Reporte de Bug's**: se ha definido una estructura solida para describir los bug's encontrados en los procesos de prueba. El reporte del bug contiene la siguiente información: Título, Versión, Descripción del Bug, Pasos para reproducir, Comportamiento Actual, Comportamiento Esperado, Capturas de Pantalla, Entonos de Prueba y Notas Adicionales; lo que permitirá al desarrollar reproducir el incidente y brindar una corrección oportuna. 
![reporte bg](https://github.com/user-attachments/assets/59229658-429d-4968-ac35-21a18cbf5cb7)

3. **Tablero de Visualización de Bug's (Bug's Board)**: se construyó un tablero especialmente para reportas los Bug's críticos de la App Urbia, el cual permitirá a cada desarrollar acceder a todos los Bug's reportados por el equipo de QA
![ac](https://github.com/user-attachments/assets/2985d999-4ff7-4cb4-9e5f-1c7373957e1d)

4. **Control y Seguimiento**: A través tanto del tablero Bug's como la lista de bug's el quipo de QA podrá saber el estado en que se encuentra cada uno e impulsar su pronta atención.
![image](https://github.com/user-attachments/assets/938efcdb-59ef-488a-a5a1-c62c3726d4fe)

---
🔗 Para más información, revisa cada archivo en la **[Carpeta Drive](https://drive.google.com/drive/folders/1YTNDdEm4klyz4-dyUw4Bq6Y8OqVfII1V?usp=sharing)**. ¡Feliz desarrollo! 🚀
