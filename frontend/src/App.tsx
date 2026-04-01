import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Dashboard from "./pages/Dashboard";
import ChatPage from "./pages/ChatPage";
import Home from "./pages/Home";
function App() {
  return (
    <BrowserRouter>
      <Routes>

        <Route path="/" element={<Home/>}></Route>
        <Route path="/login" element={<Login />} />

        <Route path="/register" element={<Register />} />

        <Route path="/dashboard" element={<Dashboard />} />

        <Route path="/chat" element={<ChatPage/>} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;