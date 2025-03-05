// lib/store/useReportsStore.js
import { create } from 'zustand';
import { devtools, persist } from 'zustand/middleware';
import { getReports } from '../api/reports/getReports';
import postReport from '../api/reports/postReport';
import putReport from '../api/reports/putReport';
import deleteReport from '../api/reports/deleteReport';
import { getReportsByUserId } from '../api/reports/getReportsById';

const STORAGE_VERSION = 2;

const useReportsStore = create(
  devtools(
    persist(
      (set) => ({
        reports: [],
        reportsByUserId: [],
        reportPreview: {},
        loading: false,
        error: null,
        fetchReports: async () => {
          set({ loading: true, error: null });
          try {
            const data = await getReports();
            set({ reports: data, loading: false });
          } catch (err) {
            set({ error: err.message, loading: false });
          }
        },
        fetchReportsByUserId: async (userId) => {
          set({ loading: true, error: null });
          try {
            const data = await getReportsByUserId(userId);
            set({ reportsByUserId: data, loading: false });
          } catch (err) {
            set({ error: err.message, loading: false });
          }
        },
        clearStorage: () => {
          localStorage.removeItem('reports-storage');
          set({ reports: [], reportsByUserId: [] });
          window.location.reload();
        },
        clearReportPreview: () => {
          set({ reportPreview: {} });
        },
        sendReport: async (data) => {
          set({ loading: true, error: null });
          try {
            const response = await postReport(data);
            set({ reportPreview: response.data, loading: false });
          } catch (error) {
            set({ error: error.message, loading: false });
          }
        },
        editReport: async (data) => {
          set({ loading: true, error: null });
          try {
            const response = await putReport(data);
            set({ reportPreview: response.data, loading: false });
          } catch (error) {
            set({ error: error.message, loading: false });
          }
        },
        deleteReport: async (id) => {
          set({ loading: true, error: null });
          try {
            await deleteReport(id);
            set({ loading: false });
          } catch (error) {
            set({ error: error.message, loading: false });
          }
        }
      }),
      {
        name: 'reports-storage',
        version: STORAGE_VERSION,
        migrate: (persisted, version) => {
          if (version < STORAGE_VERSION) {
            return {
              reports: [],
              reportsByUserId: [],
              reportPreview: {},
              loading: false,
              error: null
            };
          }
          return persisted;
        },
        partialize: (state) => ({
          reports: state.reports,
          reportsByUserId: state.reportsByUserId,
          reportPreview: state.reportPreview,
          loading: state.loading,
          error: state.error
        })
      }
    )
  )
);

export default useReportsStore;