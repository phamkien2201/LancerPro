import { ChangeEvent, KeyboardEvent, useEffect, useState } from 'react';
import { Input } from 'antd';

import ProjectList from '@/components/ProjectList/ProjectList';
import projectApi from '@/Apis/Project/Project.Api';
import { IProjectDetail } from '@/Apis/Project/Project.Interface';
import { useNavigate } from 'react-router-dom';
import { SearchOutlined } from '@ant-design/icons';

function SearchBar() {
  const [query, setQuery] = useState('');
  const [projects, setProjects] = useState<IProjectDetail[]>([]);
  const [searchResults, setSearchResult] = useState<IProjectDetail[]>([]);
  const [selectedprojectIndex, setSelectedprojectIndex] = useState<number>(-1);
  const navigate = useNavigate();

  const getProjects = async () => {
    try {
      const response = await projectApi.getProjects(0, 1000);
      setProjects(response.result.projects);
    } catch (error) {
      console.error('Error: ', error);
    }
  };

  useEffect(() => {
    getProjects();
  }, []);

  function handleQueryChange(event: ChangeEvent<HTMLInputElement>) {
    setQuery(event.target.value);
    setSearchResult(
      projects.filter((project) =>
        project.title.toLowerCase().includes(event.target.value.toLowerCase())
      )
    );
    setSelectedprojectIndex(-1);
  }

  function handleKeyDown(event: KeyboardEvent<HTMLInputElement>) {
    if (event.key === 'ArrowUp') {
      setSelectedprojectIndex((prevIndex) =>
        prevIndex === -1 ? searchResults.length - 1 : prevIndex - 1
      );
    } else if (event.key === 'ArrowDown') {
      setSelectedprojectIndex((prevIndex) =>
        prevIndex === searchResults.length - 1 ? -1 : prevIndex + 1
      );
    } else if (event.key === 'Enter') {
      if (selectedprojectIndex !== -1) {
        const selectedproject = searchResults[selectedprojectIndex];
        navigate(`/project/${selectedproject.id}`);
        setQuery('');
        setSelectedprojectIndex(-1);
        setSearchResult([]);
      }
    }
  }

  function handleProjectClick(project: IProjectDetail) {
    navigate(`/ProjectDetail/${project.id}`);
    setQuery('');
    setSelectedprojectIndex(-1);
    setSearchResult([]);
  }

  return (
    <div className="mx-6 w-full max-w-3xl">
      <Input
        allowClear
        style={{
          width: '100%',
          padding: '8px 12px',
          fontSize: '16px',
          borderRadius: '50px',
          border: '1px solid #ddd',
          boxShadow: '0 2px 5px rgba(0,0,0,0.1)',
        }}
        onChange={handleQueryChange}
        onKeyDown={handleKeyDown}
        value={query}
        placeholder="Tìm kiếm freelancer..."
        prefix={<SearchOutlined />}
      />

      {query !== '' && searchResults.length > 0 && (
        <div className="mt-2">
          <ProjectList
            projects={searchResults}
            selectedProjectIndex={selectedprojectIndex}
            handleProjectClick={handleProjectClick}
          />
        </div>
      )}

      {query !== '' && searchResults.length === 0 && (
        <div className="mt-2 text-center text-gray-500">Không có dự án nào</div>
      )}
    </div>
  );
}

export default SearchBar;
