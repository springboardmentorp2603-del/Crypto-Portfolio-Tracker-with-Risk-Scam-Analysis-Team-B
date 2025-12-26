import { useEffect, useState } from "react";
import DashboardLayout from "../layout/DashboardLayout";
import api from "../api/axios";

export default function RiskAlerts() {
  const [alerts, setAlerts] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api
      .get("/risk-alerts")
      .then((res) => setAlerts(res.data))
      .catch((err) => console.log("Risk alerts error", err))
      .finally(() => setLoading(false));
  }, []);

  const riskBadge = (level) => {
    if (level === "HIGH") return "bg-red-500/20 text-red-400";
    if (level === "MEDIUM") return "bg-yellow-500/20 text-yellow-400";
    return "bg-green-500/20 text-green-400";
  };

  return (
    <DashboardLayout>
      <div className="p-10 text-white min-h-screen cyberpunk-bg">

        {/* TITLE */}
        <div className="mb-10">
          <h1 className="text-4xl font-bold tracking-wide">
            Risk Alerts
          </h1>
          <p className="text-gray-400 mt-2">
            Potential risks detected based on market behavior
          </p>
        </div>

        {/* TABLE */}
        <div className="glass-card p-6 rounded-2xl border border-white/10 shadow-xl">
          <table className="w-full text-left">
            <thead>
              <tr className="text-gray-300 border-b border-white/10 uppercase text-sm">
                <th className="pb-4">Asset</th>
                <th className="pb-4">Risk Level</th>
                <th className="pb-4">Reason</th>
              </tr>
            </thead>

            <tbody>
              {loading && (
                <tr>
                  <td colSpan="3" className="py-10 text-center text-gray-400">
                    Analyzing market risks...
                  </td>
                </tr>
              )}

              {!loading &&
                alerts.map((a, i) => (
                  <tr
                    key={i}
                    className="border-b border-white/5 hover:bg-white/5 transition-all"
                  >
                    <td className="py-4 font-semibold">
                      {a.asset}{" "}
                      <span className="text-gray-400">
                        ({a.symbol})
                      </span>
                    </td>

                    <td>
                      <span
                        className={`px-4 py-1 rounded-full text-sm font-semibold ${riskBadge(
                          a.riskLevel
                        )}`}
                      >
                        {a.riskLevel}
                      </span>
                    </td>

                    <td className="text-gray-300">
                      {a.reason}
                    </td>
                  </tr>
                ))}
            </tbody>
          </table>
        </div>

      </div>
    </DashboardLayout>
  );
}
