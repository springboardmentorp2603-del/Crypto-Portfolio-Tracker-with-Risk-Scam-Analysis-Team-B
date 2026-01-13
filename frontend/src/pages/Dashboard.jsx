import { useEffect, useState } from "react";
import DashboardLayout from "../layout/DashboardLayout";
import api from "../api/axios";

import {
  LineChart,
  Line,
  PieChart,
  Pie,
  Cell,
  Tooltip,
  ResponsiveContainer,
  XAxis,
  YAxis,
  Legend,
} from "recharts";

export default function Dashboard() {
  const [loading, setLoading] = useState(true);
  const [summary, setSummary] = useState(null);
  const [holdings, setHoldings] = useState([]);
  const [portfolioTrend, setPortfolioTrend] = useState([]);

  const COLORS = [
    "#22c55e",
    "#3b82f6",
    "#8b5cf6",
    "#f59e0b",
    "#ef4444",
    "#06b6d4",
  ];

  const safe = (v) => Number(v || 0);

  useEffect(() => {
    Promise.all([
      api.get("/api/dashboard/summary"),
      api.get("/holdings"),
      api.get("/pricing"),
    ])
      .then(([summaryRes, holdingsRes, pricingRes]) => {
        setSummary(summaryRes.data || {});
        setHoldings(holdingsRes.data || []);

        const trend =
          pricingRes.data?.map(p => ({
            asset: p.symbol,
            value: p.priceUsd || 0,
          })) || [];

        setPortfolioTrend(trend);
      })
      .catch(err => {
        console.error("Dashboard load error", err);
      })
      .finally(() => setLoading(false));
  }, []);

  if (loading) {
    return (
      <DashboardLayout>
        <div className="p-10 text-gray-400">
          Loading dashboard...
        </div>
      </DashboardLayout>
    );
  }

  if (!summary) {
    return (
      <DashboardLayout>
        <div className="p-10 text-red-400">
          Failed to load dashboard data
        </div>
      </DashboardLayout>
    );
  }

  const holdingsDistribution = holdings.map(h => ({
    name: h.symbol,
    value: h.quantity,
  }));

  return (
    <DashboardLayout>
      <div className="p-6 text-white min-h-screen cyberpunk-bg">

        <h1 className="text-3xl font-bold mb-6">
          Dashboard Overview
        </h1>

        {/* SUMMARY CARDS */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-10">

          <div className="glass-card p-6 rounded-xl">
            <h2 className="text-gray-400">Total Portfolio Value</h2>
            <p className="text-3xl font-bold mt-2">
              ₹{safe(summary.totalPortfolioValue).toLocaleString("en-IN")}
            </p>
          </div>

          <div className="glass-card p-6 rounded-xl">
            <h2 className="text-gray-400">Realized P&amp;L</h2>
            <p
              className={`text-3xl font-bold mt-2 ${
                safe(summary.realizedPnL) >= 0
                  ? "text-green-400"
                  : "text-red-400"
              }`}
            >
              ₹{safe(summary.realizedPnL).toLocaleString("en-IN")}
            </p>
          </div>

          <div className="glass-card p-6 rounded-xl">
            <h2 className="text-gray-400">Active Holdings</h2>
            <p className="text-3xl font-bold text-purple-400 mt-2">
              {safe(summary.activeHoldings)}
            </p>
          </div>

        </div>

        {/* CHARTS */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">

          {/* PORTFOLIO TREND */}
          <div className="glass-card p-6 rounded-2xl">
            <h2 className="text-xl font-semibold mb-4">
              Portfolio Value Trend
            </h2>

            <div className="h-64">
              <ResponsiveContainer width="100%" height="100%">
                <LineChart data={portfolioTrend}>
                  <XAxis dataKey="asset" stroke="#aaa" />
                  <YAxis stroke="#aaa" />
                  <Tooltip />
                  <Line
                    type="monotone"
                    dataKey="value"
                    stroke="#22c55e"
                    strokeWidth={3}
                  />
                </LineChart>
              </ResponsiveContainer>
            </div>
          </div>

          {/* HOLDINGS DISTRIBUTION */}
          <div className="glass-card p-6 rounded-2xl">
            <h2 className="text-xl font-semibold mb-4">
              Holdings Distribution
            </h2>

            <div className="h-64">
              <ResponsiveContainer width="100%" height="100%">
                <PieChart>
                  <Pie
                    data={holdingsDistribution}
                    dataKey="value"
                    nameKey="name"
                    outerRadius={90}
                    label
                  >
                    {holdingsDistribution.map((_, index) => (
                      <Cell
                        key={index}
                        fill={COLORS[index % COLORS.length]}
                      />
                    ))}
                  </Pie>
                  <Tooltip />
                  <Legend />
                </PieChart>
              </ResponsiveContainer>
            </div>
          </div>

        </div>

      </div>
    </DashboardLayout>
  );
}
