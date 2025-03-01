// lib/store/useReportsStore.js
import { create } from 'zustand';
import { devtools, persist } from 'zustand/middleware';
import { getReports } from '../api/reports/getReports';

const STORAGE_VERSION = 2;

const useReportsStore = create(
  devtools(
    persist(
      (set, get) => ({
        reports: [],
        loading: false,
        error: null,

        fetchReports: async () => {
          set({ loading: true, error: null });
          try {
            const data = await getReports();

            console.log('✅ Reports:', data);
            set({ reports: data, loading: false });
          } catch (err) {
            set({ error: err.message, loading: false });
          }
        },


        clearStorage: () => {
          localStorage.removeItem('reports-storage');
          set({ reports: [] });
          window.location.reload();
        }
      }),
      {
        name: 'reports-storage',
        version: STORAGE_VERSION,
        migrate: (persisted, version) => {
          if (version < STORAGE_VERSION)
            return { reports: [], loading: false, error: null };
          return persisted;
        },
        partialize: state => ({
          reports: state.reports,
          loading: state.loading,
          error: state.error
        })
      }
    )
  )
);

export default useReportsStore;
