<template>
  <canvas 
    ref="canvasRef" 
    class="starry-canvas"
  ></canvas></template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'

interface Star {
  x: number
  y: number
  radius: number
  vx: number
  vy: number
  opacity: number
  twinkleSpeed: number
  twinklePhase: number
}

const canvasRef = ref<HTMLCanvasElement | null>(null)
let animationId: number | null = null
let stars: Star[] = []
const STAR_COUNT = 200

const createStar = (canvas: HTMLCanvasElement): Star => {
  return {
    x: Math.random() * canvas.width,
    y: Math.random() * canvas.height,
    radius: Math.random() * 1.5 + 0.5,
    vx: (Math.random() - 0.5) * 0.3,
    vy: (Math.random() - 0.5) * 0.3,
    opacity: Math.random() * 0.5 + 0.5,
    twinkleSpeed: Math.random() * 0.02 + 0.01,
    twinklePhase: Math.random() * Math.PI * 2
  }
}

const initStars = (canvas: HTMLCanvasElement) => {
  stars = []
  for (let i = 0; i < STAR_COUNT; i++) {
    stars.push(createStar(canvas))
  }
}

const draw = (canvas: HTMLCanvasElement, ctx: CanvasRenderingContext2D) => {
  ctx.fillStyle = 'rgba(10, 10, 30, 0.1)'
  ctx.fillRect(0, 0, canvas.width, canvas.height)

  stars.forEach((star, index) => {
    star.twinklePhase += star.twinkleSpeed
    const twinkle = Math.sin(star.twinklePhase) * 0.3 + 0.7
    const currentOpacity = star.opacity * twinkle

    const gradient = ctx.createRadialGradient(
      star.x, star.y, 0,
      star.x, star.y, star.radius * 3
    )
    gradient.addColorStop(0, `rgba(255, 255, 255, ${currentOpacity})`)
    gradient.addColorStop(0.5, `rgba(200, 200, 255, ${currentOpacity * 0.3})`)
    gradient.addColorStop(1, 'rgba(100, 100, 255, 0)')

    ctx.beginPath()
    ctx.arc(star.x, star.y, star.radius * 3, 0, Math.PI * 2)
    ctx.fillStyle = gradient
    ctx.fill()

    ctx.beginPath()
    ctx.arc(star.x, star.y, star.radius, 0, Math.PI * 2)
    ctx.fillStyle = `rgba(255, 255, 255, ${currentOpacity})`
    ctx.fill()

    star.x += star.vx
    star.y += star.vy

    if (star.x < 0) star.x = canvas.width
    if (star.x > canvas.width) star.x = 0
    if (star.y < 0) star.y = canvas.height
    if (star.y > canvas.height) star.y = 0
  })

  if (Math.random() < 0.002) {
    const shootingStar = createStar(canvas)
    shootingStar.radius = 2
    shootingStar.vx = (Math.random() - 0.5) * 8
    shootingStar.vy = Math.random() * 5 + 3
    shootingStar.opacity = 1

    for (let i = 0; i < 30; i++) {
      setTimeout(() => {
        const gradient = ctx.createRadialGradient(
          shootingStar.x, shootingStar.y, 0,
          shootingStar.x, shootingStar.y, 5
        )
        gradient.addColorStop(0, `rgba(255, 255, 255, ${1 - i / 30})`)
        gradient.addColorStop(1, 'rgba(100, 150, 255, 0)')

        ctx.beginPath()
        ctx.arc(shootingStar.x, shootingStar.y, 5, 0, Math.PI * 2)
        ctx.fillStyle = gradient
        ctx.fill()

        shootingStar.x += shootingStar.vx
        shootingStar.y += shootingStar.vy
      }, i * 20)
    }
  }

  animationId = requestAnimationFrame(() => draw(canvas, ctx))
}

const resizeCanvas = () => {
  const canvas = canvasRef.value
  if (!canvas) return

  canvas.width = window.innerWidth
  canvas.height = window.innerHeight
  initStars(canvas)
}

onMounted(() => {
  const canvas = canvasRef.value
  if (!canvas) return

  const ctx = canvas.getContext('2d')
  if (!ctx) return

  resizeCanvas()
  window.addEventListener('resize', resizeCanvas)

  ctx.fillStyle = '#0a0a1e'
  ctx.fillRect(0, 0, canvas.width, canvas.height)

  animationId = requestAnimationFrame(() => draw(canvas, ctx))
})

onUnmounted(() => {
  window.removeEventListener('resize', resizeCanvas)
  if (animationId) {
    cancelAnimationFrame(animationId)
  }
})
</script>

<style scoped>
.starry-canvas {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
  pointer-events: none;
}
</style>

