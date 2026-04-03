import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "./Login.css"; // We will create this file next

export default function Login() {
  const [companyId, setCompanyId] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();
  const BASE_URL = import.meta.env.VITE_API_URL;
  const handleLogin = async () => {
    setLoading(true);
    setError("");
    try {
      // const res = await axios.post("http://localhost:8080/api/users/login", {      
      const res = await axios.post(`${BASE_URL}/api/users/login`, {

      companyId,
        email,
        password,
      });
      localStorage.setItem("jwtToken", res.data.token);
      navigate("/dashboard");
    } catch (err) {
      setError("Invalid credentials. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-page">
      <div className="login-card">
        <header className="login-header">
          <h1>Welcome</h1>
          <p>Please enter your details to continue</p>
        </header>

        {error && <div className="error-banner">{error}</div>}

        <div className="form-container">
          {/* Boxed Field 1 */}
          

          {/* Boxed Field 2 */}
          <div className="input-group">
            <label id="email">Email Address</label>
            <input
              type="email"
              placeholder="honey@gmail.com"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          </div>

          {/* Boxed Field 3 */}
          <div className="input-group">
            <label id="password">Password</label>
            <input
              type="password"
              placeholder="••••••••"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>

          <button className="login-btn" onClick={handleLogin} disabled={loading}>
            {loading ? "Authenticating..." : "Sign In"}
          </button>
        </div>

        <footer className="login-footer">
          Don’t have an account? <a href="/register">Sign up</a>
        </footer>
      </div>
    </div>
  );
}