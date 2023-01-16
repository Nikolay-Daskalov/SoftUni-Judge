import { useState } from "react";
import Footer from "./components/footer/Footer";
import Main from "./components/main/Main";
import NavBar from "./components/navbar/NavBar";
import { useAuth } from './hooks/useAuth'
import { AuthContext } from "./contexts/AuthContext";

function App() {
  const [isLogged, setIsLogged] = useState(useAuth());

  return (
    <AuthContext.Provider value={{ isLogged, setIsLogged }}>
      <header>
        <NavBar />
      </header>
      <Main />
      <Footer />
    </AuthContext.Provider>
  );
}

export default App;