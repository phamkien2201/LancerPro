import React, { useEffect, useState } from 'react';
import { IProjectDetail } from '@/Apis/Project/Project.Interface';
import categoryApi from '@/Apis/Categories/Category.Api';

const Projectinfo: React.FC<IProjectDetail> = ({
  id,
  clientId,
  title,
  budgetMax,
  budgetMin,
  description,
  skills,
  paymentMethod,
  address,
  category,
  email,
  workPattern,
  status,
  deadline,
}) => {
  const [categoryName, setCategoryName] = useState<string>('Đang tải...');

  useEffect(() => {
    if (category) {
      categoryApi
        .getCategoryById(category)
        .then((response) => setCategoryName(response.name))
        .catch(() => setCategoryName('Không xác định'));
    }
  }, [category]);

  return (
    <div className="mt-6 flex flex-col gap-2 font-bold">
      <div className="text-xl">{title}</div>
      <div className="font-bold">
        Mô tả: <span className="font-light">{description}</span>
      </div>
      <div className="font-bold">
        Kỹ năng: <span className="font-light">{(skills || []).join(', ')}</span>
      </div>
      <div className="font-bold">
        Danh mục: <span className="font-light">{categoryName}</span>
      </div>
    </div>
  );
};

export default Projectinfo;
