
import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "./Register.css"; 

export default function Register() {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();

  const register = async () => {
    setLoading(true);
    try {
      await axios.post("http://localhost:8080/api/users/register", {
        name,
        email,
        password
      });
      alert("Registration successful!");
      navigate("/"); 
    } catch (err) {
      console.error(err);
      alert("Registration failed. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-page">
      <div className="login-card">
        <div className="login-header">
          <h1>Create Account</h1>
          <p>Please enter your details to sign up</p>
        </div>

        <div className="form-container">

          {/* BOX 2: EMAIL */}
          <div className="input-group">
            <label>Email Address</label>
            <input
              type="email"
              placeholder="honey@gmail.com"
              onChange={(e) => setEmail(e.target.value)}
            />
          </div>

          {/* BOX 3: PASSWORD */}
          <div className="input-group">
            <label>Password</label>
            <input
              type="password"
              placeholder="••••••••"
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>

          <button className="login-btn" onClick={register} disabled={loading}>
            {loading ? "Creating Account..." : "Register"}
          </button>
        </div>

        <p className="login-footer">
          Already have an account?{" "}
          <span className="link" onClick={() => navigate("/")}>
            Login
          </span>
        </p>
      </div>
    </div>
  );
}