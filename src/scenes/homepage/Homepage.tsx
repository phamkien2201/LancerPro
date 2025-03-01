import React, { useState, useEffect } from 'react';
import SearchBar from '@/components/navbar/SearchBar';
import { Typewriter } from 'react-simple-typewriter';
import CategoryApi from '@/Apis/Categories/Category.Api';
import { ICategory } from '@/Apis/Categories/Category.Interface';
import { Link, useNavigate } from 'react-router-dom';
import Items from '@/components/Items/Items';

const defaultIcons: { [key: string]: string } = {
  '67bffa811c0cd704c1bf452d':
    'https://firebasestorage.googleapis.com/v0/b/fastworkco-staging.appspot.com/o/categories%2Ffloating_icons%2Fgraphic.svg?alt=media',
  '67bffa8d1c0cd704c1bf452e':
    'https://firebasestorage.googleapis.com/v0/b/fastworkco-staging.appspot.com/o/categories%2Ffloating_icons%2Fmarketing.svg?alt=media',
  '67bffaa61c0cd704c1bf452f':
    'https://firebasestorage.googleapis.com/v0/b/fastworkco-staging.appspot.com/o/categories%2Ffloating_icons%2Fwriting.svg?alt=media',
  '67bffac01c0cd704c1bf4530':
    'https://firebasestorage.googleapis.com/v0/b/fastworkco-staging.appspot.com/o/categories%2Ffloating_icons%2Fvideo.svg?alt=media',
  '67bffad01c0cd704c1bf4531':
    'https://firebasestorage.googleapis.com/v0/b/fastworkco-staging.appspot.com/o/categories%2Ffloating_icons%2Fprogramming.svg?alt=media',
  '67bffad81c0cd704c1bf4532':
    'https://firebasestorage.googleapis.com/v0/b/fastworkco-staging.appspot.com/o/categories%2Ffloating_icons%2Fconsultant.svg?alt=media',
  '67bffae51c0cd704c1bf4533':
    'https://firebasestorage.googleapis.com/v0/b/fastworkco-staging.appspot.com/o/categories%2Ffloating_icons%2Fecommerce.svg?alt=media',
};

const HomePage: React.FC = () => {
  const navigate = useNavigate();
  const [selectedCategory, setSelectedCategory] = useState<string | null>(null);
  const [categories, setCategories] = useState<ICategory[]>([]);
  const [subCategories, setSubCategories] = useState<ICategory[]>([]);
  const [showAllSubcategories, setShowAllSubcategories] = useState(false);

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const data = await CategoryApi.getCategories();
        if (Array.isArray(data) && data.length > 0) {
          const main = data.filter((category) => category.parent_id === '0');
          const sub = data.filter((category) => category.parent_id !== '0');
          setCategories(main);
          setSubCategories(sub);
          setSelectedCategory(main[0]?.id || null);
        } else {
          console.error('API không trả về dữ liệu hợp lệ:', data);
        }
      } catch (error) {
        console.error('Lỗi khi gọi API:', error);
      }
    };
    fetchCategories();
  }, []);

  const filteredSubCategories = subCategories.filter(
    (sub) => sub.parent_id === selectedCategory
  );
  const displayedSubCategories = showAllSubcategories
    ? filteredSubCategories
    : filteredSubCategories.slice(0, 8);

  return (
    <div>
      <div className="bg-[#1677ff] px-2 py-8 text-center font-semibold text-white">
        <h1 className="text-white-400 text-3xl font-bold">
          Chúng tôi có freelancer chuyên nghiệp về...
        </h1>
        <h2 className="text-white-400 my-4 text-4xl font-bold">
          <Typewriter
            words={[
              'Lập trình',
              'Thiết kế đồ họa',
              'Viết nội dung',
              'Dịch thuật',
            ]}
            loop={true}
            cursor
            cursorStyle="|"
            typeSpeed={100}
            deleteSpeed={50}
            delaySpeed={1000}
          />
        </h2>
        <h3 className="text-100 text-xl font-bold">
          Sẵn sàng biến ý tưởng của bạn thành hiện thực
        </h3>
        <div className="mx-auto my-6 w-full max-w-[700px] flex-grow items-center">
          <SearchBar />
        </div>
      </div>

      <div
        id="homepage-category"
        className="container relative m-6 mx-auto h-[510px] h-auto max-w-[1800px] rounded-lg bg-white px-8 pb-16 pt-8 shadow-lg"
      >
        <div className="lg:grid-cols-5 grid grid-cols-2 gap-4 sm:grid-cols-3 md:grid-cols-7">
          {categories.map((category) => (
            <button
              key={category.id}
              className={`group flex flex-col items-center rounded-lg p-4 shadow-sm transition hover:shadow-md ${selectedCategory === category.id ? 'bg-blue-500 text-white' : 'bg-gray-100'}`}
              onClick={() => setSelectedCategory(category.id)}
            >
              <img
                src={defaultIcons[category.id]}
                alt={category.name}
                className="h-14 w-14 object-contain transition-transform duration-300 ease-in-out group-hover:translate-y-[-10px] group-hover:drop-shadow-lg"
              />
              <span className="mt-2 text-center">{category.name}</span>
            </button>
          ))}
        </div>

        <div className="mt-6">
          <div className="grid grid-cols-2 gap-4 sm:grid-cols-3 md:grid-cols-4">
            {displayedSubCategories.map((sub) => (
              <Link
                key={sub.id}
                to={`/category/${sub.id}`}
                className="block rounded-lg bg-gray-100 p-4 text-center shadow-md hover:bg-blue-500 hover:text-white"
              >
                {sub.name}
              </Link>
            ))}
          </div>
          {filteredSubCategories.length > 8 && (
            <button
              onClick={() => navigate('/categories')}
              className="absolute bottom-4 right-4 mt-8 rounded-md border border-blue-30 bg-white px-3 py-1 text-xs font-medium text-blue-30 hover:bg-blue-30 hover:text-white"
            >
              Xem thêm
            </button>
          )}
        </div>
      </div>
      <Items />
    </div>
  );
};

export default HomePage;
