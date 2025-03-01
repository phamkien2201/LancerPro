import {
  IProject,
  // IProjectResponse,
  IProjects,
  IProjectDetail,
} from './Project.Interface';
import { apiService } from '@/configs/apiService';

export default {
  getProjects(
    page?: number,
    limit?: number,
    sortBy?: string,
    sortDirection?: string,
  ): Promise<IProject> {
    return apiService
      .get(`/projects/get-all`, {
        params: {
          page,
          limit,
          sortBy,
          sortDirection,
        },
      })
      .then((response) => response.data);
  },

  // getProductBrand(page: number, limit: number): Promise<IProductResponse> {
  //   return apiService
  //     .get(`/products/get-all-products`, {
  //       params: { page, limit },
  //     })
  //     .then((response) => response.data.products);
  // },

  getById(id: string): Promise<IProjects> {
    return apiService.get(`/projects/${id}`).then((response) => {
      console.log('API response for project:', response.data);
      return response.data;
    });
  },

  getCategory(
    categoryId: string,
    page?: number,
    limit?: number,
    sortBy?: string,
    sortDirection?: string,
  ): Promise<IProject> {
    return apiService
      .get(`/projects/category/${categoryId}`, {
        params: {
          categoryId,
          page,
          limit,
          sortBy,
          sortDirection,
        },
      })
      .then((response) => {
        console.log('API:', response);
        return response.data;
      });
  },

  getByClientId(
    clientId: string,
    page?: number,
    limit?: number,
    sortBy?: string,
    sortDirection?: string,
  ): Promise<IProject> {
    return apiService
      .get(`/projects/client/${clientId}`, {
        params: {
          page,
          limit,
          sortBy,
          sortDirection,
          clientId,
        },
      })
      .then((response) => {
        console.log('API:', response);
        return response.data;
      });
  },

   updateProject(id: string, data: IProjectDetail): Promise<IProjects> {
      return apiService
        .put(`/projects/update/${id}`, data)
        .then((response) => {
          console.log('API response:', response);
          return response.data;
        });
    },
    updateProjectStatus(id: string, data: IProjectDetail): Promise<IProjects> {
      return apiService
        .put(`/projects/${id}/status`, data)
        .then((response) => {
          console.log('API response:', response);
          return response.data;
        });
    },
    deleteProject(id: string): Promise<void> {
      return apiService
        .delete(`/projects/delete/${id}`)
        .then((response) => {
          console.log('Project deleted successfully:', response);
        });
    },

};
