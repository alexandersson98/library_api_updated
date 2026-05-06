import './style.css'
import { navigate } from './router.js'

const getPage = () => location.hash.slice(1) || 'authors'

navigate(getPage())

window.addEventListener('hashchange', () => navigate(getPage()))
