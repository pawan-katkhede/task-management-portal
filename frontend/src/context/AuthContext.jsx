import { createContext, useState } from 'react';
import api from '../services/api';
import toast from 'react-hot-toast';

export const AuthContext = createContext();

const getInitialUser = () => {
  const token = localStorage.getItem('token');
  const email = localStorage.getItem('userEmail');
  if (token && email) return { email };
  return null;
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(getInitialUser);

  const login = async (email, password) => {
    try {
      const response = await api.post('/auth/login', { email, password });
      if (response.data.success) {
        localStorage.setItem('token', response.data.token);
        localStorage.setItem('userEmail', response.data.email);
        setUser({ email: response.data.email });
        toast.success('Login successful!');
        return true;
      }
      toast.error(response.data.message || 'Login failed');
      return false;
    } catch (err) {
      toast.error(err.response?.data?.message || 'Login failed');
      return false;
    }
  };

  const register = async (email, password, confirmPassword) => {
    try {
      const response = await api.post('/auth/register', { email, password, confirmPassword });
      if (response.data.success) {
        // No auto-login - user must login manually
        toast.success('Registration successful! Please login.');
        return true;
      }
      toast.error(response.data.message || 'Registration failed');
      return false;
    } catch (err) {
      toast.error(err.response?.data?.message || 'Registration failed');
      return false;
    }
  };

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('userEmail');
    setUser(null);
    toast.success('Logged out successfully');
  };

  return (
    <AuthContext.Provider value={{ user, login, register, logout, loading: false }}>
      {children}
    </AuthContext.Provider>
  );
};