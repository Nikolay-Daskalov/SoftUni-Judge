import { useState } from "react";
import style from '../common/form.module.css';
import { useNavigate } from "react-router-dom";

function Register() {
    const [user, setUser] = useState({
        username: '',
        email: '',
        password: '',
        confirmPassword: ''
    });
    const [info, setInfo] = useState(undefined);
    const navigate = useNavigate();

    const onChange = (e) => {
        setUser((state) => ({ ...state, [e.target.name]: [e.target.value] }));
    };

    const onSubmit = (e) => {
        e.preventDefault();

        const userData = Object.fromEntries(new FormData(e.target));

        if (userData.password !== userData.confirmPassword) {
            setInfo('Passwords don`t match');
            return;
        }

        const url = 'http://localhost:8080/api/users/register';

        fetch(url, {
            method: 'POST',
            mode: 'cors',
            headers: {
                "Content-type": "application/json"
            },
            body: JSON.stringify(userData)
        })
            .then(res => {
                if (res.ok) {
                    navigate('/users/login');
                } else {
                    setInfo('Username or Email already exist');
                }
            })
            .catch(err => {
                console.log(err);
            });
    };

    return (
        <div className={"text-center ms-auto me-auto mt-5 border border-info border-3 rounded shadow " + style['form-div-wrapper']}>
            <h1 className="fw-bold mb-4 mt-3">Registration Form</h1>
            <form onSubmit={onSubmit}>
                <div className={"form-floating mb-3 ms-auto me-auto " + style['field-wrapper']}>
                    <input value={user.username} onChange={onChange} type="text" className="form-control" id="username"
                        placeholder="Username" name="username" required minLength="3" maxLength="40" />
                    <label htmlFor="username">Username</label>
                    <div className="form-text">Username must be 3-40 characters long.</div>
                </div>
                <div className={"form-floating mb-3 ms-auto me-auto " + style['field-wrapper']}>
                    <input value={user.email} onChange={onChange} type="email" className="form-control" id="email"
                        placeholder="Email" name="email" required minLength="3" maxLength="254" />
                    <label htmlFor="email">Email</label>
                    <div className="form-text">Email must be at most 254 characters long.</div>
                </div>
                <div className={"form-floating mb-3 ms-auto me-auto " + style['field-wrapper']}>
                    <input value={user.password} onChange={onChange} type="password" className="form-control" id="password" placeholder="Password" name="password"
                        required minLength="3" maxLength="25" />
                    <label htmlFor="password">Password</label>
                    <div className="form-text">Password must be 3-25 characters long.</div>
                </div>
                <div className={"form-floating mb-3 ms-auto me-auto " + style['field-wrapper']}>
                    <input value={user.confirmPassword} onChange={onChange} type="password" className="form-control" id="confirmPassword" placeholder="Confirm Password" name="confirmPassword"
                        required minLength="3" maxLength="25" />
                    <label htmlFor="confirmPassword">Confirm Password</label>
                    <div className="form-text">Confirm Password must be 3-25 characters long.</div>
                </div>
                {info ?
                    <p className="fs-5 mb-4">{info}</p>
                    : undefined}
                <button type="submit" className="btn btn-primary mb-4">Register</button>
            </form>
        </div>
    );
}

export default Register;