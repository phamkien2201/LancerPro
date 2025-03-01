import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { Button, Divider, Tag } from 'antd';
import { IProjectDetail } from '@/Apis/Project/Project.Interface';
import projectApi from '@/Apis/Project/Project.Api';
import moment from 'moment';

const Items: React.FC = () => {
  const [Projects, setProjects] = useState<IProjectDetail[]>([]);

  const getProjects = async () => {
    try {
      const response = await projectApi.getProjects(
        0,
        200,
        'createdAt',
        'desc'
      );
      setProjects(response.result.projects.slice(0, 4)); // Chỉ lấy 4 project
    } catch (error) {
      console.error('Error: ', error);
    }
  };

  useEffect(() => {
    getProjects();
  }, []);

  return (
    <div className="px-8">
      <Divider
        orientation="center"
        style={{ fontSize: '36px', fontWeight: '700', paddingTop: '25px' }}
      >
        Các công việc được quan tâm nhất
      </Divider>

      <div className="flex flex-col items-center space-y-6">
        {Projects.map((project) => (
          <div
            key={project.id}
            className="flex w-full max-w-5xl gap-6 rounded-lg border bg-white p-6 shadow-lg"
          >
            {/* Cột bên trái (70%) */}
            <div className="w-[70%]">
              <Link
                to={`/ProjectDetail/${project.id}`}
                className="block text-xl font-semibold text-blue-600 hover:text-blue-800"
              >
                {project.title}
              </Link>

              <div className="mt-3 text-base text-gray-700">
                📍 <strong>Địa điểm:</strong> {project.address}
              </div>

              <div className="mt-3 text-base text-gray-700">
                💰 <strong>Ngân sách:</strong>{' '}
                {project.budgetMin.toLocaleString()}đ -{' '}
                {project.budgetMax.toLocaleString()}đ
              </div>

              <div className="mt-3 text-base text-gray-700">
                💼 <strong>Thanh toán:</strong> {project.paymentMethod}
              </div>

              <div className="mt-3 text-base text-gray-700">
                ⏳ <strong>Hạn chót:</strong>{' '}
                {moment(project.deadline).format('DD/MM/YYYY')}
              </div>

              <div className="mt-3">
                🛠 <strong>Kỹ năng:</strong>
                <div className="mt-1 flex flex-wrap gap-2">
                  {project.skills?.map((skill) => (
                    <Tag key={skill} color="blue" className="text-base">
                      {skill}
                    </Tag>
                  ))}
                </div>
              </div>
            </div>

            {/* Cột bên phải (30%) */}
            <div className="w-[30%] border-l pl-6">
              <div className="mt-3 text-sm text-gray-600">
                📧 <strong>Email:</strong> {project.email}
              </div>

              <div className="mt-3 text-sm text-gray-600">
                ⏰ <strong>Ngày tạo:</strong>{' '}
                {moment(project.createdAt).format('DD/MM/YYYY')}
              </div>

              <div className="mt-3 text-sm text-gray-600">
                🔄 <strong>Trạng thái:</strong>{' '}
                <span
                  className={`font-bold ${
                    project.status === 'open'
                      ? 'text-green-600'
                      : 'text-red-600'
                  }`}
                >
                  {project.status === 'open' ? 'Đang mở' : 'Đã đóng'}
                </span>
              </div>
            </div>
          </div>
        ))}
      </div>

      <div className="flex justify-center pt-6">
        <Link to="/BestSell">
          <Button className="mb-4 rounded-xl border border-blue-30 bg-white px-6 py-4 text-base font-bold text-blue-30 hover:!bg-blue-30 hover:!text-white">
            Xem Thêm...
          </Button>
        </Link>
      </div>
    </div>
  );
};

export default Items;
