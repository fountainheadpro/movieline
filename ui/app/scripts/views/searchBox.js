  /** @jsx React.DOM */

  var SearchBox = React.createClass({displayName: 'SearchBox',
    getInitialState: function() {
      return {
        prompt: "Zed's dead...",
        query: ""
      };
    },    

    handleChange: function() {
        //this.props.onUserInput(
        //   this.refs.filterTextInput.getDOMNode().value,            
        //);
    },    

    render: function() {
        return (
            React.DOM.form(null, 
              React.DOM.div( {className:"searchContainer"} , 
                React.DOM.input( 
                  {type:"text", 
                  className:"searchBar", 
                  placeholder:this.state.prompt, 
                  ref:"filterTextInput",
                  value:this.props.query} ),
                React.DOM.span( {className:"glyphicon glyphicon-search searchButton"})
              )  
            )
        );
    }
  });


var Scene=React.createClass({displayName: 'Scene',
  render: function(){
    return (
          React.DOM.div( {className:"col-sm-6 col-md-6"}, 
            React.DOM.div( {className:"thumbnail", onClick:this.handleClick},       
              React.DOM.img( {src:this.props.scene.imageSource, alt:this.props.scene.caption}),                          
              React.DOM.blockquote(null, 
                React.DOM.p(null, this.props.scene.caption)
              )
            )
          )                   
    )    
  },

  handleClick: function(event) {
    
  },

});  

/** @jsx React.DOM */
var Clip = React.createClass({displayName: 'Clip',
    render: function() {
      ///var scope = this.props.scope;
      var images = [];
      this.props.scope.data.forEach(function(scene){
        images.push(
              Scene( {scene:scene, key:scene.key})
            )
      }.bind(this));
      return (
          React.DOM.div( {className:"thumbnails"}, 
            images
          )
      )      
    }
});