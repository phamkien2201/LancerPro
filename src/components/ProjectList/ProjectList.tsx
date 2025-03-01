import { useEffect } from 'react';
import { IProjectDetail } from '@/Apis/Project/Project.Interface';
type ProjectListsProps = {
  selectedProjectIndex: number;
  projects: IProjectDetail[];
  handleProjectClick: (project: IProjectDetail) => void;
};

const ProjectList = ({
  projects,
  selectedProjectIndex,
  handleProjectClick,
}: ProjectListsProps) => {
  const scrollActiveProjectIntoView = (index: number) => {
    const activeProject = document.getElementById(`project-${index}`);
    console.log(`scrollIntoView called for project-${index}`, activeProject);
    if (activeProject) {
      activeProject.scrollIntoView({
        block: 'nearest',
        inline: 'start',
        behavior: 'smooth',
      });
    } else {
      console.log(`Element with id project-${index} not found`);
    }
  };

  useEffect(() => {
    if (selectedProjectIndex !== -1) {
      scrollActiveProjectIntoView(selectedProjectIndex);
    }
  }, [selectedProjectIndex]);

  return (
    <div className="resultProjectContainer max-h-96 overflow-y-scroll bg-white">
      {projects.map((project, index) => (
        <div
          key={project.id}
          id={`project-${index}`}
          className={`${selectedProjectIndex === index ? 'bg-gray-200' : ''} flex cursor-pointer items-center justify-between gap-8 px-4 py-2 hover:bg-gray-200`}
          onClick={() => handleProjectClick(project)}
        >
          <img
            src={project.skills?.[0] || '/default-image.jpg'}
            alt={project.title}
            className="w-8"
          />
          <p className="truncate-limit-custom2">{project.title}</p>

          <p>{project.budgetMin.toLocaleString()}đ</p>
          <p>{project.budgetMax.toLocaleString()}đ</p>
        </div>
      ))}
    </div>
  );
};
export default ProjectList;
