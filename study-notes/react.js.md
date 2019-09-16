## create a new react app

first use npm to install a tool for setting up the scaffolding of a new app

```
npm install -g create-react-app
```

then go to projects directory, create a new app

```
create-react-app some-app
```

cd into it and there you go!




A subclass of Component will have to override render() method. This will be called every time the state of the component is changed to re-render the component in its up-to-date state.
Don't change state of a component directly; instead, use setState() which will trigger the relevant re-rendering.
