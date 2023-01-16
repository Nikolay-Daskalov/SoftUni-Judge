

export const useAuth = () => {
    return localStorage.getItem('accessToken') !== null;
};