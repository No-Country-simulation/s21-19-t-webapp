import { useState } from 'react'
import { Label } from '@/components/ui/label'
import { Input } from '@/components/ui/input'
import { Textarea } from '@/components/ui/textarea'
import { Button } from '@/components/ui/button'

export default function ReportForm() {
  const [titulo, setTitulo] = useState('')
  const [descripcion, setDescripcion] = useState('')
  const [latitud, setLatitud] = useState('')
  const [longitud, setLongitud] = useState('')
  const [categoriaId, setCategoriaId] = useState('')
  const [usuarioId, setUsuarioId] = useState('')
  const [audio, setAudio] = useState(null)
  const [imagen, setImagen] = useState(null)

  const handleSubmit = async (e) => {
    e.preventDefault()

    const reporteDTO = {
      titulo,
      descripcion,
      latitud,
      longitud,
      categoriaId: Number(categoriaId),
      usuarioId: Number(usuarioId)
    }

    const formData = new FormData()
    if (audio) formData.append('audio', audio)
    if (imagen) formData.append('imagen', imagen)
    formData.append(
      'reporte',
      new Blob([JSON.stringify(reporteDTO)], { type: 'application/json' })
    )

    try {
      const response = await fetch('https://urbia-back.onrender.com/api/reporte/combinado', {
        method: 'POST',
        body: formData
      })
      if (response.ok) {
        const data = await response.json()
        console.log(data)
      } else {
        console.error('Error al enviar reporte', response.statusText)
      }
    } catch (error) {
      console.error('Error', error)
    }
  }

  return (
    <form
      onSubmit={handleSubmit}
      className="max-w-md mx-auto p-4 bg-white rounded shadow flex flex-col gap-4"
    >
      <div>
        <Label htmlFor="titulo">Título</Label>
        <Input
          id="titulo"
          placeholder="Título"
          value={titulo}
          onChange={(e) => setTitulo(e.target.value)}
        />
      </div>
      <div>
        <Label htmlFor="descripcion">Descripción</Label>
        <Textarea
          id="descripcion"
          placeholder="Descripción"
          value={descripcion}
          onChange={(e) => setDescripcion(e.target.value)}
        />
      </div>
      <div>
        <Label htmlFor="latitud">Latitud</Label>
        <Input
          id="latitud"
          placeholder="Latitud"
          value={latitud}
          onChange={(e) => setLatitud(e.target.value)}
        />
      </div>
      <div>
        <Label htmlFor="longitud">Longitud</Label>
        <Input
          id="longitud"
          placeholder="Longitud"
          value={longitud}
          onChange={(e) => setLongitud(e.target.value)}
        />
      </div>
      <div>
        <Label htmlFor="categoriaId">Categoría</Label>
        <Input
          id="categoriaId"
          placeholder="Categoría"
          value={categoriaId}
          onChange={(e) => setCategoriaId(e.target.value)}
        />
      </div>
      <div>
        <Label htmlFor="usuarioId">Usuario</Label>
        <Input
          id="usuarioId"
          placeholder="Usuario"
          value={usuarioId}
          onChange={(e) => setUsuarioId(e.target.value)}
        />
      </div>
      <div>
        <Label htmlFor="audio">Audio</Label>
        <Input
          id="audio"
          type="file"
          accept="audio/*"
          onChange={(e) => setAudio(e.target.files[0])}
        />
      </div>
      <div>
        <Label htmlFor="imagen">Imagen</Label>
        <Input
          id="imagen"
          type="file"
          accept="image/*"
          onChange={(e) => setImagen(e.target.files[0])}
        />
      </div>
      <Button type="submit">Enviar Reporte</Button>
    </form>
  )
}
