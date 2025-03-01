import { IProject, IProjects, ProjectDetail } from './AProject.interface';
import { apiService } from '@/configs/apiService';

export default {
  getProjects(page: number, limit: number): Promise<IProject> {
    return apiService
      .get(`/projects/get-all`, {
        params: { page, limit },
      })
      .then((response) => response.data);
  },

  getProjectsByCategoryId(categoryId: string, page: number, limit: number): Promise<IProject> {
    return apiService
      .get(`/projects/categoryId/${categoryId}`, {
        params: { categoryId, page, limit },
      })
      .then((response) => response.data);
  },

  getProjectsByClientId(clientId: string, page: number, limit: number): Promise<IProject> {
    return apiService
      .get(`/projects/client/${clientId}`, {
        params: { clientId, page, limit },
      })
      .then((response) => response.data);
  },

  getProjectsById(id: string, page: number, limit: number): Promise<IProject> {
    return apiService
      .get(`/projects/${id}`, {
        params: { id, page, limit },
      })
      .then((response) => response.data);
  },

  createProject(data: ProjectDetail): Promise<IProjects> {
    return apiService
      .post(`/projects/create`, data)
      .then((response) => {
        console.log('API response:', response);
        return response.data;
      })
      .catch((error) => {
        console.error('Error creating project:', error);
        throw error;
      });
  },

  updateProjectById(id: string, data: ProjectDetail): Promise<IProjects> {
    return apiService
      .put(`/projects/update/${id}`, data)
      .then((response) => {
        console.log('API response:', response);
        return response.data;
      });
  },
  deleteProjectById(id: string): Promise<void> {
    return apiService
      .delete(`/projects/delete/${id}`)
      .then((response) => {
        console.log('Project deleted successfully:', response);
      });
  },
};
