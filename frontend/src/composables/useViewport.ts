import { ref, onMounted, onUnmounted } from 'vue'

type Breakpoints = {
  sm: number
  md: number
  lg: number
  xl: number
}

const defaultBreakpoints: Breakpoints = {
  sm: 640,
  md: 768,
  lg: 1024,
  xl: 1280
}

export function useViewport(breakpoints = defaultBreakpoints) {
  const viewportWidth = ref(window.innerWidth)
  const isMobile = ref(window.innerWidth < breakpoints.md)
  
  const handler = () => {
    viewportWidth.value = window.innerWidth
    isMobile.value = window.innerWidth < breakpoints.md
  }

  const debounce = (fn: Function, delay = 100) => {
    let timeout: number
    return () => {
      clearTimeout(timeout)
      timeout = setTimeout(fn, delay)
    }
  }

  const debouncedHandler = debounce(handler)

  onMounted(() => window.addEventListener('resize', debouncedHandler))
  onUnmounted(() => window.removeEventListener('resize', debouncedHandler))

  return {
    viewportWidth,
    isMobile,
    breakpoints
  }
}
