import { useContext } from "react";
import { Link, NavLink } from "react-router-dom";
import { AuthContext } from "../../contexts/AuthContext";

function NavBar() {
    const auth = useContext(AuthContext);

    const isLoggedHandler = () => {
        if (auth.isLogged) {
            const logoutHandler = () => {
                localStorage.removeItem('accessToken');
                localStorage.removeItem('username');
                auth.setIsLogged(false);
            };

            return (
                <li className="nav-item ms-lg-3 mb-sm-1 mb-lg-0 ms-auto me-auto">
                    <NavLink className="nav-link active fs-5" to="/" onClick={logoutHandler}>Logout</NavLink>
                </li>
            );
        } else {
            return (
                <li className="nav-item ms-lg-3 mb-sm-1 mb-lg-0 ms-auto me-auto">
                    <NavLink className="nav-link active fs-5" to="/users/login">Login</NavLink>
                </li>
            );
        }
    };

    return (
        <nav className="navbar navbar-expand-lg navbar-light bg-info pt-0 pb-0">
            <div className="container-fluid">
                <Link className="navbar-brand ms-4 fs-1 fw-bold fst-italic" to="/">Trading212 Judge</Link>
                <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                    id="mobile-nav-button">
                    <span className="navbar-toggler-icon"></span>
                </button>
                <div className="collapse navbar-collapse text-center" id="navbarNav">
                    <ul className="navbar-nav ms-auto me-4">
                        <li className="nav-item dropdown ms-lg-3 mb-sm-1 mb-lg-0 ms-auto me-auto">
                            <ul className="dropdown-menu text-center p-1" aria-labelledby="navbarDropdown">
                                <li>
                                    <NavLink className="dropdown-item" to="/documents" >Documents</NavLink>
                                </li>
                                <li>
                                    <hr className="dropdown-divider mt-1 mb-1" />
                                </li>
                            </ul>
                        </li>
                        <li className="nav-item ms-lg-3 mb-sm-1 mb-lg-0 ms-auto me-auto">
                            <NavLink className="nav-link active fs-5 position-relative" to="/documents" >Documents</NavLink>
                        </li>
                        {isLoggedHandler()}
                    </ul>
                </div>
            </div>
        </nav>
    );
}

export default NavBar;