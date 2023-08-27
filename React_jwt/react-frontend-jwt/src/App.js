import { BrowserRouter, Route, Routes } from 'react-router-dom';
import './App.scss';

import Login from './pages/Auth/Login';
import Register from './pages/Auth/Register';

function App() {
  return (
    <div className="App">
      <BrowserRouter>
      <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
      </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
