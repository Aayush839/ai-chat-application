import { Link } from "react-router-dom";
import "./Home.css";

const Home = () => {
  return (
    <div className="home-container">
      <div className="home-content">
        <div className="icon-badge">AI</div>
        <h1>Welcome to AI Chat App</h1>
        <p>Experience the next generation of intelligent conversation.</p>

        <div className="button-group">
          <Link to="/login" className="btn-primary">
            Login to Account
          </Link>
          
          <Link to="/register" className="btn-outline">
            Create New Account
          </Link>
        </div>
      </div>
    </div>
  );
};

export default Home;