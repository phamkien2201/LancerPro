import Navbar from '@/components/navbar';
import { Navigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Homepage from './scenes/homepage/Homepage';
import ProjectDetail from './components/ProjectDetail/ProjectDetail';
import Footer from './components/Footer/Footer';
import LoginPage from './components/Loginout/Login';
import RegisterPage from './components/Loginout/Register';
import CustomerInfo from './components/Customer/CustomerInfor';
import AdminLayout from './components/Admin/AdminLayout';
import UserList from './components/Admin/Dashboard/UserList';
import React from 'react';

const checkTokenAndRole = () => {
  const token = localStorage.getItem('accessToken');
  const roles = JSON.parse(localStorage.getItem('roles') || '[]');

  const isAdmin = roles.some((role: { name: string }) => role.name === 'ADMIN');

  return { token, isAdmin };
};

const PrivateAdminRoute = ({ children }: { children: React.ReactNode }) => {
  const { token, isAdmin } = checkTokenAndRole();

  if (!token || !isAdmin) {
    return <Navigate to="/login" replace />;
  }

  return <>{children}</>;
};

const App = () => {
  const [isTopOfPage, setIsTopOfPage] = useState(false);

  useEffect(() => {
    const handleScroll = () => {
      setIsTopOfPage(window.scrollY === 0);
    };

    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  const isAdminPath = location.pathname.startsWith('/admin');

  return (
    <div className="flex min-h-screen flex-col">
      <Router>
        {!isAdminPath && (
          <header className="fixed top-0 z-10 w-full">
            <Navbar isTopOfPage={isTopOfPage} />
          </header>
        )}
        <main className={`flex-grow ${isAdminPath ? '' : 'mt-[140px]'}`}>
          <Routes>
            <Route path={`/`} element={<Homepage />} />
            <Route path={`/ProjectDetail/:id`} element={<ProjectDetail />} />
            <Route path={`/Login`} element={<LoginPage />} />
            <Route path={`/register`} element={<RegisterPage />} />
            <Route path={`/CustomerInfo`} element={<CustomerInfo />} />
            <Route
              path="/admin"
              element={
                <PrivateAdminRoute>
                  <AdminLayout />
                </PrivateAdminRoute>
              }
            >
              <Route path="users" element={<UserList />} />
            </Route>
          </Routes>
        </main>
        {!isAdminPath && (
          <footer className="drop-shadow">
            <Footer />
          </footer>
        )}
      </Router>
    </div>
  );
};

export default App;
