const { merge } = require('webpack-merge');
const common = require('./webpack.config.js');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CopyPlugin = require('copy-webpack-plugin');
const path = require("path");

module.exports = merge(common,{
  mode: 'development',
  devtool: 'source-map',
  devServer: {
    liveReload: true,
    hot: true,
    port: 8001,
    open: true,
    static: ['./'],
  },
  entry: {
    app: path.join(__dirname,'Test.ts'),
  },
  output: {
    path: path.resolve(__dirname, 'dist'),
    clean: true,
    filename: 'js/bundle.js',
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: path.join(__dirname,'index.html'),
    })
  ],
});
