import { Route, Routes } from "react-router-dom";
import Greeting from "../greeting/Greeting";
import Register from "../register/Register";
import NotFound from '../error/NotFound';
import Login from "../login/Login";

function Main() {
    return (
        <main>
            <Routes>
                <Route path="/" element={<Greeting />} />
                <Route path="/users/register" element={<Register />} />
                <Route path="/users/login" element={<Login />} />
                <Route path="*" element={<NotFound />} />
            </Routes>
        </main>
    );
}

export default Main;