const path = require('path');

module.exports = {
  mode:"production",
  entry: {
    app: path.join(__dirname,'js','typelessHTMLFormatter.ts'),
  },
  module: {
    rules:   [
      {
        loader: 'ts-loader',
        exclude: /node_modules/,
        test: /\.ts?$/
      }
    ]
  },
  resolve: {
    extensions: ['.ts', '.js']
  },
  output: {
    path: path.resolve(__dirname, 'dist'),
    clean: true,
    library: 'typelessHTMLFormatter',
    filename: 'typelessHTMLFormatter.js',
  },
};
