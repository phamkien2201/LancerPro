import { useEffect, useState } from 'react';
import Logo from '@/assets/Lance-logo.png';
import { Bars3Icon, XMarkIcon } from '@heroicons/react/24/solid';
import useMediaQuery from '@/hooks/useMediaQuery';
import CategoryApi from '@/Apis/Categories/Category.Api';
import { useNavigate } from 'react-router-dom';
import { ICategory } from '@/Apis/Categories/Category.Interface';
import {
  UserOutlined,
  LogoutOutlined,
  BellOutlined,
  MessageOutlined,
} from '@ant-design/icons';
import { Link } from 'react-router-dom';
import { Drawer, Menu, Badge } from 'antd';

type Props = {
  isTopOfPage: boolean;
};

const Navbar = ({ isTopOfPage }: Props) => {
  const flexBetween = 'flex items-center justify-between';
  const [username, setUsername] = useState<string | null>(null);
  const [isMenuToggled, setIsMenuToggled] = useState<boolean>(false);
  const [mainCategories, setMainCategories] = useState<ICategory[]>([]);
  const [subCategories, setSubCategories] = useState<ICategory[]>([]);
  const [cartCount, setCartCount] = useState<number>(0);
  const isAboveMediumScreens = useMediaQuery('(min-width:1060px)');
  const navigate = useNavigate();
  const [showCategoryNav, setShowCategoryNav] = useState(false);

  useEffect(() => {
    const handleScroll = () => {
      const categorySection = document.getElementById('homepage-category');
      if (categorySection) {
        const rect = categorySection.getBoundingClientRect();
        setShowCategoryNav(rect.bottom <= 0); // Khi category trên homepage không còn trong viewport
      }
    };

    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const data = await CategoryApi.getCategories();
        if (Array.isArray(data) && data.length > 0) {
          const main = data.filter((category) => category.parent_id === '0');
          const sub = data.filter(
            (category) =>
              category.parent_id !== '0' &&
              main.some((parent) => parent.id === category.parent_id)
          );

          setMainCategories(main);
          setSubCategories(sub);
        } else {
          console.error('API không trả về dữ liệu hợp lệ:', data);
        }
      } catch (error) {
        console.error('Lỗi khi gọi API:', error);
      }
    };
    fetchCategories();
  }, []);

  useEffect(() => {
    const savedUsername = localStorage.getItem('username');
    setUsername(savedUsername);
  }, []);

  const handleLogout = () => {
    if (window.confirm('Bạn có chắc chắn muốn đăng xuất?')) {
      localStorage.removeItem('username');
      localStorage.removeItem('userId');
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');
      localStorage.removeItem('expiryTime');
      window.location.href = '/login';
    }
  };

  return (
    <nav className="fixed top-0 w-full bg-gradient-to-b from-[#013d9f] to-[#1677ff] pt-4">
      <div className={`${flexBetween} mx-8`}>
        <div className={`${flexBetween} ml-6 w-full gap-4`}>
          <img
            alt="logo"
            src={Logo}
            className="w-33 mb-4 h-20 cursor-pointer"
            onClick={() => navigate('/')}
          />

          <div className="ml-6 flex items-center gap-6">
            <Link to="/Cart" className="flex items-center">
              <Badge count={cartCount} showZero>
                <BellOutlined className="cursor-pointer text-2xl text-white" />
              </Badge>
            </Link>
            <Link to="/Cart" className="flex items-center">
              <Badge count={cartCount} showZero>
                <MessageOutlined className="cursor-pointer text-2xl text-white" />
              </Badge>
            </Link>

            {username ? (
              <div className="flex items-center gap-4 text-white">
                <Link
                  to="/CustomerInfo"
                  className="flex items-center hover:underline"
                >
                  <UserOutlined className="mr-2 text-lg text-white" />
                  {username}
                </Link>
                <button
                  onClick={handleLogout}
                  className="flex items-center gap-2 rounded-lg border bg-white px-3 py-1 text-sm font-medium text-red-500 hover:border-red-700"
                >
                  Đăng xuất
                  <LogoutOutlined />
                </button>
              </div>
            ) : (
              <Link
                to="/Login"
                className="rounded-lg border bg-white px-4 py-1 text-sm font-medium text-blue-10 hover:bg-gray-100 hover:text-blue-30"
              >
                <UserOutlined className="mr-2" />
                Đăng nhập
              </Link>
            )}
          </div>
        </div>
      </div>
      {isAboveMediumScreens && showCategoryNav && (
        <div className="relative flex items-center justify-around bg-white py-1 shadow-md">
          <ul className="flex cursor-pointer flex-wrap gap-3 capitalize">
            {mainCategories.map((category) => (
              <li key={category.id} className="group">
                <Link to={`/category/${category.id}`}>
                  <div className="px-2 py-2 text-center font-semibold hover:text-blue-30">
                    {category.name}
                  </div>
                </Link>
                <div className="absolute left-0 top-full hidden w-full bg-white p-4 shadow-lg group-hover:block">
                  {subCategories.some(
                    (sub) => sub.parent_id === category.id
                  ) && (
                    <ul className="grid grid-cols-5 gap-5 p-2 pl-10">
                      {subCategories
                        .filter((sub) => sub.parent_id === category.id)
                        .map((sub) => (
                          <li
                            key={sub.id}
                            className="rounded-lg p-2 hover:bg-gray-100 hover:text-blue-30"
                          >
                            <Link to={`/category/${sub.id}`}>{sub.name}</Link>
                          </li>
                        ))}
                    </ul>
                  )}
                </div>
              </li>
            ))}
          </ul>
        </div>
      )}
      {/* : (
      <>
        <button
          className="flex rounded-full bg-red-400 p-3"
          onClick={() => setIsMenuToggled(!isMenuToggled)}
        >
          <div className="h-6 w-6">
            {isMenuToggled ? <XMarkIcon /> : <Bars3Icon />}
          </div>
        </button>

        <Drawer
          title="Danh mục"
          placement="left"
          onClose={() => setIsMenuToggled(false)}
          open={isMenuToggled}
          width={300}
        >
          <Menu mode="inline" theme="light">
            {mainCategories.map((category) => (
              <Menu.SubMenu
                key={category.id}
                title={
                  <Link to={`/category/${category.id}`}>{category.name}</Link>
                }
              >
                {subCategories
                  .filter((sub) => sub.parent_id === category.id)
                  .map((sub) => (
                    <Menu.Item key={sub.id}>
                      <Link to={`/category/${sub.id}`}>{sub.name}</Link>
                    </Menu.Item>
                  ))}
              </Menu.SubMenu>
            ))}
          </Menu>
        </Drawer>
      </>
      ){'}'} */}
    </nav>
  );
};

export default Navbar;
