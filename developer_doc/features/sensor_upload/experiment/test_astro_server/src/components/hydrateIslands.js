// 在岛屿加载完成后移除骨架
document.addEventListener('DOMContentLoaded', () => {
    const islands = document.querySelectorAll('astro-island');
    
    islands.forEach(island => {
      island.addEventListener('astro:load', () => {
        const skeleton = island.previousElementSibling;
        if (skeleton && skeleton.classList.contains('skeleton-container')) {
          skeleton.remove();
        }
      });
    });
  });