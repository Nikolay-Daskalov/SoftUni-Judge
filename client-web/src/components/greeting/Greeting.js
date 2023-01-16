function Greeting() {
    const getUsername = () => localStorage.getItem('username');

    const username = getUsername();

    return (
        <div className="text-center ms-auto me-auto mt-5">
            {username ?
                <h1 className="fw-bold mb-4 mt-3">Hi again, {username}</h1>
                : <h1 className="fw-bold mb-4 mt-3">Welcome</h1>}
        </div>
    );

}

export default Greeting;