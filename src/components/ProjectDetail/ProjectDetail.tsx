import React, { useEffect, useState } from 'react';
import { Tabs } from 'antd';
import Projectinfo from './Projectinfo';
import projectApi from '@/Apis/Project/Project.Api';
import { IProjects } from '@/Apis/Project/Project.Interface';
import { useParams } from 'react-router-dom';
import userApi from '@/Apis/Auth/Auth.Api';

const { TabPane } = Tabs;

const ProjectDetail: React.FC = () => {
  const [project, setProject] = useState<IProjects | null>(null);
  const [clientName, setClientName] = useState<string>('');
  const { id } = useParams<{ id: string }>();

  useEffect(() => {
    if (id) {
      projectApi
        .getById(id)
        .then((response) => {
          setProject(response);
          if (response.result?.clientId) {
            userApi.getUserById(response.result.clientId).then((user) => {
              setClientName(user.result.name || 'Không xác định');
            });
          }
        })
        .catch((error) => console.error('Error fetching project:', error));
    }
  }, [id]);

  if (!project || !project.result) {
    return (
      <div className="flex h-screen items-center justify-center text-lg font-semibold">
        Loading...
      </div>
    );
  }

  return (
    <div className="container mx-auto max-w-6xl rounded-lg bg-white p-8 shadow-lg">
      <div className="grid grid-cols-12 gap-8">
        {/* Cột trái - Thông tin dự án (70%) */}
        <div className="col-span-8 rounded-lg bg-gray-100 p-6 shadow-md">
          <h2 className="mb-4 text-2xl font-semibold text-gray-700">
            Thông tin dự án
          </h2>
          <Projectinfo {...project.result} />
        </div>

        {/* Cột phải - Thông tin khách hàng (30%) */}
        <div className="col-span-4 rounded-lg border bg-gray-50 p-6 shadow-md">
          <h2 className="mb-4 text-xl font-semibold text-gray-700">
            Thông tin khách hàng
          </h2>
          <div className="space-y-3 text-sm">
            <p>
              <span className="font-medium text-gray-600">ID Dự án:</span>{' '}
              {project.result.id}
            </p>
            <p>
              <span className="font-medium text-gray-600">Tên khách hàng:</span>{' '}
              {clientName}
            </p>
            <p>
              <span className="font-medium text-gray-600">Email:</span>{' '}
              {project.result.email}
            </p>
            <p>
              <span className="font-medium text-gray-600">Ngân sách:</span>{' '}
              {project.result.budgetMin.toLocaleString()}đ -{' '}
              {project.result.budgetMax.toLocaleString()}đ
            </p>
            <p>
              <span className="font-medium text-gray-600">
                Phương thức thanh toán:
              </span>{' '}
              {project.result.paymentMethod}
            </p>
            <p>
              <span className="font-medium text-gray-600">
                Địa chỉ làm việc:
              </span>{' '}
              {project.result.address || 'Chưa có địa chỉ'}
            </p>
            <p>
              <span className="font-medium text-gray-600">
                Hình thức làm việc:
              </span>{' '}
              {project.result.workPattern || 'Không xác định'}
            </p>
            <p>
              <span className="font-medium text-gray-600">Trạng thái:</span>{' '}
              {project.result.status}
            </p>
            <p>
              <span className="font-medium text-gray-600">Hạn nộp:</span>{' '}
              {new Date(project.result.deadline).toLocaleString()}
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProjectDetail;
